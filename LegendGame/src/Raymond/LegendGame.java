package Raymond;

import java.io.*;

public class LegendGame {
    private static C_Player s_player;  
    private static final String SAVE_FILE = "savegame.txt"; 
    private static BufferedReader s_reader = new BufferedReader(new InputStreamReader(System.in));  

  
    public static void main(String args[]) 
    {
        System.out.println("仙劍奇俠傳 傳說:十里坡劍神");
        
        // 嘗試讀取存檔
        s_player = loadGameFromText();
        
        if (s_player == null)  // 無存檔，建立新角色
        {
            String v_inputName = "李逍遙";  // 預設角色名稱
            System.out.print("請輸入姓名 (預設: 李逍遙): ");
            try {
                String v_line = s_reader.readLine();
                if(v_line != null && !v_line.trim().isEmpty()) 
                {
                    v_inputName = v_line.trim();
                }
            } catch(IOException e) 
            {
                System.out.println("輸入錯誤，使用預設名稱。");
            }
            s_player = new C_Player(v_inputName);
        } 
        else  // 讀取存檔
        {
            System.out.println("讀取存檔成功！歡迎回歸江湖，" + s_player.getName());
        }

        // === 主遊戲迴圈 ===
        boolean v_playing = true;
        while (v_playing) 
        {
            System.out.println("\n【十里坡】 Lv: " + s_player.getLevel());
            System.out.println("1. 戰鬥");
            System.out.println("2. 休息");
            System.out.println("3. 狀態");
            System.out.println("4. 離開");
            System.out.print("> ");

            try {
                String v_choice = s_reader.readLine();
                if (v_choice == null) continue;

                switch (v_choice.trim()) 
                {
                    case "1":  
                        battle();
                        break;
                    case "2":  
                        s_player.recover();
                        System.out.println("回嬸嬸旅館恢復體力");
                        break;
                    case "3":  
                        System.out.println("HP: " + s_player.getCurrentHp() + "/" + s_player.getMaxHp());
                        System.out.println("MP: " + s_player.getCurrentMp() + "/" + s_player.getMaxMp());
                        System.out.println("攻擊力: " + s_player.getAttackPower());
                        break;
                    case "4":  
                        System.out.println("正在儲存遊戲進度...");
                        saveGameAsText(s_player);
                        v_playing = false; 
                        System.out.println("存檔完成。江湖路遙，後會有期！");
                        break;
                    default:
                        System.out.println("無效指令，請輸入 1~4");
                }
            }
            catch (IOException e)
            {
                System.out.println("輸入錯誤: " + e.getMessage());
            } 
            catch (Exception e) 
            {
                System.out.println("發生未預期的錯誤: " + e.getMessage());
            }
        }
    }

    /**
     *  戰鬥系統
     */
    private static void battle() 
    {
        C_Monster v_enemy = C_MonsterFactory.spawnRandomMonster();  
        System.out.println("\n遭遇 " + v_enemy.getName() + "!");

        //  戰鬥方式選擇
        while (v_enemy.isAlive() && s_player.isAlive()) {
            System.out.println("--------------------------------");
            System.out.println("我方: HP " + s_player.getCurrentHp() + "/" + s_player.getMaxHp() + 
                               "  MP " + s_player.getCurrentMp() + "/" + s_player.getMaxMp());
            System.out.println("敵方: " + v_enemy.getName() + " HP " + v_enemy.getCurrentHp());
            System.out.println("--------------------------------");
            System.out.println("[1] 普攻  [2] 招式  [3] 逃跑");
            System.out.print("> ");
            
            try 
            {
                String v_act = s_reader.readLine();
                v_act = v_act.trim();

                if(v_act.equals("1"))  
                {
                    s_player.attack(v_enemy);
                } 
                else if(v_act.equals("2"))  
                {
                    System.out.println("--- 招式列表 ---");
                    int v_count = 0;
                    int[] v_skillIndices = new int[20];  // 儲存可用招式在 s_SKILL_DATA 中的索引
                    
                    // 列出所有已學會的技能
                    for(int i=0; i<C_Player.s_SKILL_DATA.length; i++)
                    {
                        int v_reqLvl = (int)C_Player.s_SKILL_DATA[i][2];  // 取得招式需求等級（索引2）
                        if(s_player.getLevel() >= v_reqLvl)  // 玩家等級足夠才顯示此技能
                        {
                            v_skillIndices[v_count] = i;  // 記錄招式索引
                            v_count++;
                            System.out.println(v_count + ". " + C_Player.s_SKILL_DATA[i][0] + 
                                " (消耗MP:" + C_Player.s_SKILL_DATA[i][1] + ")");
                        }
                    }
                    
                    if(v_count == 0)  // 沒有可用招式
                    {
                        System.out.println("無可用招式");
                    } 
                    else 
                    {
                        System.out.print("選擇 (輸入編號): ");
                        try 
                        {
                            String v_selStr = s_reader.readLine();
                            if(v_selStr != null)
                            {
                                int v_sel = Integer.parseInt(v_selStr.trim());
                                if(v_sel > 0 && v_sel <= v_count)  // 驗證輸入範圍是否有效
                                {
                                    s_player.castSkill(v_skillIndices[v_sel-1], v_enemy);
                                } 
                                else
                                {
                                    System.out.println("無效的編號");
                                }
                            }
                        } 
                        catch(NumberFormatException e)
                        {
                            System.out.println("請輸入數字！");
                        }
                    }
                } 
                else if(v_act.equals("3"))  // 選項3: 逃跑
                {
                    System.out.println("逃跑成功！");
                    return;  // 結束戰鬥
                } 
                else 
                {
                    System.out.println("無效指令");
                }
            
                // 怪物回合（如果還存活就進行反擊）
                if(v_enemy.isAlive()) v_enemy.attack(s_player);

            } 
            catch (IOException e) 
            {
                System.out.println("讀取輸入失敗");
            }
        }

        // === 戰鬥結束處理 ===
        if(s_player.isAlive())  // 玩家獲勝
        {
            s_player.gainExp(v_enemy.getExpReward());  // 獲得經驗值
            
            // 檢查是否達成劍神成就
            if(s_player.hasMasteredSwordGod()) 
            {
                System.out.println("\n======================================");
                System.out.println("已取得十里坡劍神榮耀 江湖任你行");
                System.out.println("========================================");
            }
        } 
        else  // 玩家戰敗
        {
            System.out.println("戰鬥失敗... (勝敗乃兵家常事)");
            s_player.recover();  // 自動復活並恢復滿血滿真氣
        }
    }

    /**
     * 儲存遊戲進度到文字檔
     * 格式: 名稱,等級,當前HP,當前MP
     */
    private static void saveGameAsText(C_Player p_player)
    {
        try (BufferedWriter v_writer = new BufferedWriter(new FileWriter(SAVE_FILE))) 
        {
            // 將角色資料組合成 CSV 格式字串
            String v_saveData = p_player.getName() + "," + 
                                p_player.getLevel() + "," + 
                                p_player.getCurrentHp() + "," + 
                                p_player.getCurrentMp();
            
            v_writer.write(v_saveData);
            v_writer.flush();
        } 
        catch (IOException e)
        {
            System.out.println("存檔失敗: " + e.getMessage());
        }
    }

    /**
     * 從文字檔讀取遊戲進度
     * @return 讀取成功返回 C_Player 物件，失敗返回 null
     */
    private static C_Player loadGameFromText() 
    {
        File v_file = new File(SAVE_FILE);
        if (!v_file.exists()) return null;  // 存檔檔案不存在

        try (BufferedReader v_reader = new BufferedReader(new FileReader(v_file)))
        {
            String v_line = v_reader.readLine();
            if (v_line != null && !v_line.isEmpty())
            {
                String[] v_data = v_line.split(",");  // 解析 CSV 格式
                if (v_data.length >= 4)
                {
                    String v_name = v_data[0];
                    int v_level = Integer.parseInt(v_data[1]);
                    int v_hp = Integer.parseInt(v_data[2]);
                    int v_mp = Integer.parseInt(v_data[3]);
                    return new C_Player(v_name, v_level, v_hp, v_mp);
                }
            }
        } 
        catch (IOException | NumberFormatException e) 
        {
            System.out.println("讀檔錯誤 (檔案損毀或格式不符): " + e.getMessage());
        }
        return null;
    }
}
