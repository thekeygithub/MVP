function showTime(){
	var ds = new DateSelector("idYear", "idMonth", "idDay", {
		MaxYear: new Date().getFullYear(),
		MinYear:1900
	});
}