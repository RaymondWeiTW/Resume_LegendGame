package Raymond;

public class C_Player extends A_GameCharacter {
    private int m_exp;              // 當前經驗值
    private int m_nextLevelExp;     // 升級所需經驗值

    /*
     * 招式表
     * 使用 Object 為了在同一陣列中混合存放 String 與 int 型別
  	 * ( 招式名稱 ,MP消耗 ,等級需求 ,技能威力或回復量 ,技能型態(0=攻擊, 1=回復))
     */
    public static final Object[][] s_SKILL_DATA =  
    {
        {"氣療術", 6, 1, 75, 1},
        {"御劍術", 10, 2, 154, 0},
        {"萬劍訣", 25, 7, 340, 0},
        {"凝神歸元", 18, 11, 220, 1},
        {"天師符法", 18, 12, 280, 0},
        {"元靈歸心術", 40, 17, 500, 1},
        {"天劍", 44, 22, 606, 0},
        {"逍遙神劍", 50, 30, 800, 0},
        {"劍神", 64, 34, 900, 0}, 
    };

    /**
     * 建構子 多載 (無資料)
     * 初始化角色名稱
     */
    public C_Player(String p_name)
    {
        super(p_name, 1, 100, 50, 15, 5);  
        this.m_exp = 0;               
        this.m_nextLevelExp = 50;    
    }

    /**
     * 建構子 多載 (有資料) 
     *(角色名稱, 等級, 血量, 真氣)
     * 由於血量 真氣 攻擊 真氣 攻擊 防禦 都是用等級計算產生 故使用for迴圈累加計算
     */
    public C_Player(String p_name, int p_level, int p_currentHp, int p_currentMp)
    {
        super(p_name, 1, 100, 50, 15, 5);  
        this.m_exp = 0;
        this.m_nextLevelExp = 50;
        
        // for迴圈累加計算
        for(int i = 1; i < p_level; i++)
        {
            this.m_level++;
            this.m_maxHp = m_maxHp + 25;             
            this.m_maxMp = m_maxMp + 15;              
            this.m_attackPower = m_attackPower + 6;   
            this.m_defense = m_defense + 3;           
            this.m_nextLevelExp = (int)(this.m_nextLevelExp * 1.2);  
        }
        
        // ****在考慮是否要直接用累加計算值 目前暫定先使用原來資料 待升級後更新****
        this.m_currentHp = p_currentHp;
        this.m_currentMp = p_currentMp;
    }

    /**
     * 檢查是否升級
     */
    public void gainExp(int p_amount)
    {
        this.m_exp += p_amount;
        System.out.println("獲得經驗值 " + p_amount);
        
        if(this.m_exp >= this.m_nextLevelExp)
        {
            levelUp();
        }
    }

    /**
     * 升級處理 - 提升角色等級並增加屬性
     */
    private void levelUp() 
    {
        this.m_exp = this.m_exp - this.m_nextLevelExp;  
        this.m_level++;                    
        this.m_maxHp = m_maxHp + 25;             
        this.m_maxMp = m_maxMp + 15;              
        this.m_attackPower = m_attackPower + 6;   
        this.m_defense = m_defense + 3;     

        this.m_currentHp = this.m_maxHp;
        this.m_currentMp = this.m_maxMp;
 
        this.m_nextLevelExp = (int)(this.m_nextLevelExp * 1.2);
     
        System.out.println("\n=== 升級！等級 " + this.m_level + " ===");
        
        // 檢查條件可以學到新招式
        for(int i = 0; i < s_SKILL_DATA.length; i++)
        {
            Object[] v_skill = s_SKILL_DATA[i];

            if((int)v_skill[2] == this.m_level)
            {
                System.out.println("★ 領悟新招式：[" + v_skill[0] + "]");
            }
        }

    }
    
    /**
     * 檢查是否學到劍神
     */
    public boolean hasMasteredSwordGod()
    {
        return this.m_level >= 34;  
    }

    /**
     * 普通攻擊
     * 傷害公式: max(1, 攻擊力 - 目標防禦力) 至少造成1點傷害
     */
    public void attack(A_GameCharacter p_target)
    {
        int v_dmg = Math.max(1, this.m_attackPower - p_target.getDefense());
        
        System.out.println(this.m_name + " 普通攻擊 " + p_target.getName() + "，造成 " + v_dmg + " 點傷害！");
        p_target.takeDamage(v_dmg);
    }

    /**
     * 施展招能
     * 新設區域變數取得招式表
     * 判斷 是否為攻擊招式 回復招式
     */
    public void castSkill(int p_skillIndex, A_GameCharacter p_target) 
    {
        Object[] v_skill = s_SKILL_DATA[p_skillIndex];
        String v_sName = (String)v_skill[0];    
        int v_mpCost = (int)v_skill[1];         
        int v_power = (int)v_skill[3];          
        int v_type = (int)v_skill[4];           

        // 檢查真氣
        if(this.m_currentMp < v_mpCost) 
        {
            System.out.println("真氣不足！需要 " + v_mpCost);
            return;
        }

        // 施展招式
        this.consumeMp(v_mpCost);
        System.out.println(this.m_name + " 施展了 [" + v_sName + "]！");

        if(v_type == 1)  // 回復類型 
        { 
            // 回復
            this.m_currentHp = Math.min(this.m_maxHp, this.m_currentHp + v_power);
            System.out.println("回復了 " + v_power + " 點體力！");
        } 
        else  
        { 
            // 攻擊 
        	// 傷害公式 : (技能威力 + 攻擊力) - 目標防禦力
            int v_totalDmg = (v_power + this.m_attackPower) - p_target.getDefense();
            
            System.out.println("擊中 " + p_target.getName() + "，造成 " + v_totalDmg + " 點傷害！");
            p_target.takeDamage(v_totalDmg);
        }
    }
}
