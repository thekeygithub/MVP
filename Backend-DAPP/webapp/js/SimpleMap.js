function Entry(key,val){
	this._key=key;
	this._value=val;
}
Entry.prototype.toString=function(){
	return this._key+"-"+this._value;
}
function SimpleMap(){
	this.entrys=[];
}
SimpleMap.prototype.put=function(key,val){
	for(var i=0;i<this.entrys.length;i++){
		if(this.entrys[i]._key==key){
			this.entrys[i]._value=val;
			return;
		}
	}
	this.entrys[this.entrys.length]=new Entry(key,val);
}
SimpleMap.prototype.get=function(key){
	for(var i=0;i<this.entrys.length;i++){
		if(this.entrys[i]._key==key){
			return this.entrys[i]._value;
		}
	}
	return "";
}
SimpleMap.prototype.size=function(){
	return this.entrys.length;
}
SimpleMap.prototype.keySet=function(){
	var keys=[];
	for(var i=0;i<this.entrys.length;i++)
		keys[keys.length]=this.entrys[i]._key;
	return keys;
}
SimpleMap.prototype.getEntrys=function(){
	return this.entrys;
}
SimpleMap.prototype.remove=function(key){
	var i=0;
	for(;i<this.entrys.length;i++){
		if(this.entrys[i]._key==key)
			break;
	}
	if(i<this.entrys.length){
		for(;i<this.entrys.length-1;i++)
			this.entrys[i]=this.entrys[i+1];
		this.entrys.length=this.entrys.length-1;
	}
}
SimpleMap.prototype.clear=function(){
	this.entrys=[];
}
/*
var s=new SimpleMap();
s.put("1","11");
s.put("2","22");
s.put("3","33");
s.put("2","dddd");
document.write(s.getEntrys());
s.clear();
document.write("=="+s.getEntrys());*/