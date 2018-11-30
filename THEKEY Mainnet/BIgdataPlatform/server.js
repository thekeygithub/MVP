var ethers = require('ethers');
var express = require("express");
var bodyParser = require("body-parser");
var app = express();
app.use(bodyParser.urlencoded({ extended: false }));
var hostName = '192.168.99.217';
var port = 8911;
var http = require('http');
var url = require("url");
var querystring = require("querystring");

app.all('*', function(req, res, next) {
    res.header("Access-Control-Allow-Origin", "*");
    res.header("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept");
    res.header("Access-Control-Allow-Methods","PUT,POST,GET,DELETE,OPTIONS");
    res.header("X-Powered-By",' 3.2.1')
    res.header("Content-Type", "application/json;charset=utf-8");
    next();
});




var testJson = {
	'Name':"test",
	'Id':"140101199001010051",
	'Gender':"Female",
	'Cellphone':"18700000000",
	'Company':"sinopec",
	'Residence':"",
	'Blood':"A",
	'Education':"Master",
	'Career':"Engineer",
	'Marry':"true",
	'Medical_Type':"city_employee",
	'Allergies':{
		'Penicillin':"false",
		'Sulfonamide':"false",
		'Streptomycin':"false",
		'Others':"none"
	},
	'Past_Edical_History':{
		'Disease':{
      'Hypertension':"false",
      'Diabetes':"false",
      'HeartDisease':"false",
      'LungDisease':"false",
      'Cancer':"false",
      'BrainStroke':"false",
      'Neuropathy':"false",
      'Tuberculosis':"false",
      'Hepatitis':"false",
      'Others':"false"
    },
		'Surgery':{
		},
		'Trauma':{
		},
		'BloodTran':{
		}
	},
	'Family':{
		'Father':{
      'Hypertension':"false",
      'Diabetes':"false",
      'HeartDisease':"false",
      'LungDisease':"false",
      'Cancer':"false",
      'BrainStroke':"false"
    },
		'Mother':{
      'Hypertension':"false",
      'Diabetes':"false",
      'HeartDisease':"false",
      'LungDisease':"false",
      'Cancer':"false",
      'BrainStroke':"false"
    },
		'Brothers':{
      'Hypertension':"false",
      'Diabetes':"false",
      'HeartDisease':"false",
      'LungDisease':"false",
      'Cancer':"false",
      'BrainStroke':"false"
    },
		'Children':{
      'Hypertension':"false",
      'Diabetes':"false",
      'HeartDisease':"false",
      'LungDisease':"false",
      'Cancer':"false",
      'BrainStroke':"false"
    }
	}
};



app.get("/HealthInfo",function(req,res){
    let userid = req.query.userid;
    let username = req.query.username;
    let usergender = req.query.usergender;
    let userphone = req.query.userphone;
    let usercompany = req.query.usercompany;

    var jsonStr = JSON.stringify(zhangsanJson);
    var zsStr = JSON.parse(jsonStr);
    zsStr.Name = username;
    zsStr.Id = userid;
    zsStr.Gender = usergender;
    zsStr.Cellphone = userphone;
    zsStr.Company = usercompany;

    jsonStr = JSON.stringify(zsStr);
    var result = {code:200,msg:jsonStr};
    res.send(result);
});


app.post("/getHealthInfo",function(req,res){
    let userid = req.body.userid;
    let username = req.body.username;
    let usergender = req.body.usergender;
    let userphone = req.body.userphone;
    let usercompany = req.body.usercompany;

    var jsonStr = JSON.stringify(zhangsanJson);
    var zsStr = JSON.parse(jsonStr);
    zsStr.Name = username;
    zsStr.Id = userid;
    zsStr.Gender = usergender;
    zsStr.Cellphone = userphone;
    zsStr.Company = usercompany;

    jsonStr = JSON.stringify(zsStr);

    var result = {code:200,msg:jsonStr};
    res.send(result);
});




app.listen(port,hostName,function(){
   console.log(`服务器运行在http://${hostName}:${port}`);
});
