package Raymond;

public abstract class A_GameCharacter  // 抽象類別 - 定義遊戲基本屬性與行為
{
    
    protected String m_name;           
    protected int m_level;             
    protected int m_maxHp;             
    protected int m_currentHp;         
    protected int m_maxMp;             
    protected int m_currentMp;         
    protected int m_attackPower;       
    protected int m_defense;           

    /**
     * 建構子 初始化角色屬性
     * (名稱, 等級, 血量, 真氣質, 攻擊, 防禦)
     */
    public A_GameCharacter(String p_name, int p_lvl, int p_hp, int p_mp, int p_atk, int p_def)
    {
        this.m_name = p_name;
        this.m_level = p_lvl;
        
        this.m_maxHp = p_hp; 
        this.m_currentHp = p_hp;      
        this.m_maxMp = p_mp; 
        this.m_currentMp = p_mp;  
        
        this.m_attackPower = p_atk;
        this.m_defense = p_def;
    }

    /**
     * 判斷角色是否存活
     */
    public boolean isAlive()
    { 
        return m_currentHp > 0; 
    }

    /**
     * 受傷機制 
     */
    public void takeDamage(int p_dmg)
    {
        this.m_currentHp = m_currentHp - p_dmg;
        if (this.m_currentHp < 0)  
        {
            this.m_currentHp = 0;
        }
    }

    /**
     * 消耗真氣值 
     */
    public void consumeMp(int p_amount)
    {
        this.m_currentMp = m_currentMp - p_amount;
        if(this.m_currentMp < 0) 
        {
            this.m_currentMp = 0;
        }
    }

    /**
     * 回旅館 恢復滿血量與滿真氣
     */
    public void recover()
    {
        this.m_currentHp = this.m_maxHp;
        this.m_currentMp = this.m_maxMp;
    }

    /**
     *Get 方法
     */
    
    //取得角色名稱
    public String getName()
    { 
        return m_name; 
    }
    // 取得角色等級
    public int getLevel()
    { 
        return m_level;
    }
    //取得當前生命
    public int getCurrentHp()
    { 
        return m_currentHp; 
    }
    //取得當前真氣
    public int getCurrentMp()
    {
        return m_currentMp;
    }
    // 取得最大生命值
    public int getMaxHp()
    { 
        return m_maxHp;
    }
    // 取得最大真氣值
    public int getMaxMp()
    { 
        return m_maxMp; 
    }
     // 取得攻擊力
    public int getAttackPower()
    { 
        return m_attackPower;
    }
     //取得防禦力
    public int getDefense()
    { 
        return m_defense; 
    }
}
