package Raymond;

public class C_MonsterFactory {
    /*
     * 生成隨機怪物
     * (名稱, 等級, 最大HP, 最大MP, 攻擊力, 防禦力, 經驗值)
     * 40% 蜜蜂 ,30% 酒罈妖 ,30% 燈籠怪
     */
    public static C_Monster spawnRandomMonster() 
    {
        double v_rng = Math.random(); // 
        
        if (v_rng < 0.4)  
        {
            return new C_Monster("蜜蜂", 1, 40, 0, 10, 0, 3);
        }
        else if (v_rng < 0.7)  
        {
            return new C_Monster("酒罈妖", 2, 60, 0, 15, 2, 5);
        }
        else  
        {
            return new C_Monster("燈籠怪", 3, 80, 0, 20, 5, 8);
        }
    }
}
