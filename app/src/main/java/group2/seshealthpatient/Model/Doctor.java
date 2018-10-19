package group2.seshealthpatient.Model;

public class Doctor {
    private String fullname;
    private String age;
    private String gender;
    private String profession;
    private String BriefIntruduction;
    private String address;
    private String PhoneNumber;
    private String id;

    public String getId() {
        return id;
    }

    public String getBriefIntruduction() {
        return BriefIntruduction;
    }

    public void setBriefIntruduction(String briefIntruduction) {
        BriefIntruduction = briefIntruduction;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Doctor(){

    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return PhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        PhoneNumber = phoneNumber;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }
}
