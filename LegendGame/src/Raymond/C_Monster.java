package Raymond;

public class C_Monster extends A_GameCharacter {
    private int m_expReward;  // 擊敗此怪物後給予玩家的經驗值

    /**
     * 建構子 - 初始化怪物
     *( 怪物名稱 ,怪物等級 ,最大生命值 ,最大真氣值 ,攻擊力 ,防禦力 ,經驗值 )
     */
    public C_Monster(String p_name, int p_lvl, int p_hp, int p_mp, int p_atk, int p_def, int p_exp) 
    {
        super(p_name, p_lvl, p_hp, p_mp, p_atk, p_def);  // 呼叫父類別建構子
        this.m_expReward = p_exp;
    }

    /**
     * 攻擊目標角色
     * 傷害計算公式: 實際傷害 = max(1, 攻擊力 - 目標防禦力)
     */
    public void attack(A_GameCharacter p_target) 	//從A_GameCharacter 找到對應的怪物
    {
        int v_dmg = Math.max(1, this.m_attackPower - p_target.getDefense());				//保證受傷是一點
        
        System.out.println(this.m_name + " 攻擊 " + p_target.getName() + "，造成 " + v_dmg + " 點傷害！");
        p_target.takeDamage(v_dmg);
    }

    /**
     * 取得擊敗此怪物的經驗值獎勵
     */
    public int getExpReward() 
    {
        return m_expReward;
    }
}
