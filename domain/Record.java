package domain;

public class Record {
    private String userName,characterType,time;

    public Record(String userName, String characterType) {
        this.userName = userName;
        this.characterType = characterType;
    } // constructor

    public String getUserName() {
        return this.userName;
    } // getUserName

    public void setUserName(String userName) {
        this.userName = userName;
    } // setUserName

    public String getCharacterType() {
        return this.characterType;
    } // getCharacterType

    public void setCharacterType(String characterType) {
        this.characterType = characterType;
    } // setCharacterType

    public String getTime() {
        return time;
    } // getTime

    public void setTime(String time) {
        this.time = time;
    } // setTime
    
} // end class
