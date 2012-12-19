package jp.ac.iwasaki.android.safetycard;

public class SCRecKiroku {

    long _id;
    String name;
    String tel_no;
    String address;
    String latitude;
    String longitude;
    String car_no;
    String date;
    String time;
    String place;
    String other;
    byte[] photo1;
    byte[] photo2;
    byte[] photo3;
    
	public SCRecKiroku(long _id, String name, String tel_no, String address,
    		         String latitude, String longitude,
					 String car_no, String date, String time, String place,
    		         String other, byte[] photo1, byte[] photo2, byte[] photo3) {
        this._id     = _id;
        this.name    = name;
        this.tel_no  = tel_no;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.car_no  = car_no;
        this.date    = date;
        this.time    = time;
        this.place   = place;
        this.other   = other;
        this.photo1  = photo1;
        this.photo2  = photo2;
        this.photo3  = photo3;
    }
    
    public long getId() {
        return _id;
    }

	public String getName() {
		return name;
	}

	public String getTel_no() {
		return tel_no;
	}

	public String getAddress() {
		return address;
	}

	public String getCar_no() {
		return car_no;
	}

	public String getDate() {
		return date;
	}

	public String getTime() {
		return time;
	}

	public String getPlace() {
		return place;
	}

	public String getOther() {
		return other;
	}

	public byte[] getPhoto1() {
		return photo1;
	}

	public byte[] getPhoto2() {
		return photo2;
	}

	public byte[] getPhoto3() {
		return photo3;
	}

	public void set_id(long _id) {
		this._id = _id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setTel_no(String tel_no) {
		this.tel_no = tel_no;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public void setCar_no(String car_no) {
		this.car_no = car_no;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public void setPlace(String place) {
		this.place = place;
	}

	public void setOther(String other) {
		this.other = other;
	}

	public void setPhoto1(byte[] photo1) {
		this.photo1 = photo1;
	}

	public void setPhoto2(byte[] photo2) {
		this.photo2 = photo2;
	}

	public void setPhoto3(byte[] photo3) {
		this.photo3 = photo3;
	}

    public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

}
