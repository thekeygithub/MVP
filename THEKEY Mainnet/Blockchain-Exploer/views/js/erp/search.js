var oldSearchTypeTxt="";
var oldSearchGoodsTxt="";
function searchTxtClick(){
    if ( $( "#searchTypeTxt" ).val() == '输入分类名称的拼音缩写' ) {
	    $("#searchTypeTxt").val("");
	}
}

function searchTxtBlur(){
  if ( $("#searchTypeTxt").val() == '' ) {
     $("#searchTypeTxt").val("输入分类名称的拼音缩写");
  }
}

function searchGoodsTxtClick(){
    if ( $( "#searchGoodsTxt" ).val() == '输入商品名称的拼音缩写或条形编码' ) {
	    $("#searchGoodsTxt").val("");
	}
}

function searchGoodsTxtBlur(){
  if ( $("#searchGoodsTxt").val() == '' ) {
     $("#searchGoodsTxt").val("输入商品名称的拼音缩写或条形编码");
  }
}


function searchTypeSuggest(e){
  try{
   if(e.keyCode==40){
     document.getElementById('selectType').focus();  
     document.getElementById('selectType').selectedIndex=0;
    return false;
   }
  }catch(ee){
  }
  var sText=escape( $("#searchTypeTxt").val() );
  if(oldSearchTypeTxt==sText) return ;
  oldSearchTypeTxt=sText;
  if( sText == '' ) {
   $('#search_suggestType').html("");
   $("#search_suggestType").hide();
   return ;
  }
  sText = sText.toUpperCase();
  var ss = document.getElementById('search_suggestType');
  ss.innerHTML = '';
  var n=0;
  var suggest='';
  for ( var i=0;i<typeArr.length;i++ ) {
    if ( typeArr[i].pinyin.indexOf(sText) == 0 ) {
      n=n+1;
      suggest += '<option value="'+typeArr[i].type_id+'">';
      if ( typeArr[i].type_level == 1 ) {
	    suggest += typeArr[i].name;
	  } else {
	    suggest += top.getTypeNameById( typeArr[i].parent_id ) + "->" + typeArr[i].name;
	  }
      suggest += '</option>';
      if(n==12) break; 
    }
  }
  if ( suggest!='' ) {
   ss.innerHTML='<select id="selectType" name="selectType" size="6" style="overflow:auto;width:178px" onkeydown="keydown(event,2)" onkeypress="keypress(event,2)" onclick="getSelectedType()"  >'+ suggest+'</select>';
   ss.style.display="";
   $("#search_suggestType").show();
  } else {
   $("#search_suggestType").hide();
  }
}

function searchGoodsSuggest(e){
  try{
   if(e.keyCode==40){
     document.getElementById('selectGoods').focus();  
     document.getElementById('selectGoods').selectedIndex=0;
    return false;
   }
  }catch(ee){
  }
  var sText=escape( $("#searchGoodsTxt").val() );
  if( oldSearchGoodsTxt==sText ) return ;
  oldSearchGoodsTxt=sText;
  if( sText == '' ) {
   $('#search_suggestGoods').html("");
   $("#search_suggestGoods").hide();
   return ;
  }
  sText = sText.toUpperCase();
  var ss = document.getElementById('search_suggestGoods');
  ss.innerHTML = '';
  var n=0;
  var suggest='';
  var allGoodsArr=top.getAllGoodsArr();
  for ( var i=0;i<allGoodsArr.length;i++ ) {
    if ( allGoodsArr[i].pinyin.indexOf(sText) == 0 || allGoodsArr[i].barcode.indexOf(sText) == 0 ) {
      n=n+1;
      suggest += '<option value="'+allGoodsArr[i].goods_id+'">';
	  suggest += allGoodsArr[i].name;
	  if ( allGoodsArr[i].specification != '' ) suggest+="("+allGoodsArr[i].specification+")";
	  suggest += "&nbsp;单价:"+allGoodsArr[i].price;
      suggest += '</option>';
      if(n==12) break; 
    }
  }
  if ( suggest!='' ) {
   ss.innerHTML='<select id="selectGoods" name="selectGoods" size="6" style="overflow:auto;width:256px" onkeydown="keydown(event,1)" onkeypress="keypress(event,1)" onclick="getSelectedGoods()"  >'+ suggest+'</select>';
   ss.style.display="";
   $("#search_suggestGoods").show();
  } else {
   $("#search_suggestGoods").hide();
  }
}

function getSelectedGoods(){
  $("#search_suggestGoods").hide();
  doSelectedGoods( top.getGoodsById( $("#selectGoods").val() ) );
  $("#searchGoodsTxt").val("输入商品名称的拼音缩写或条形编码");
  oldSearchGoodsTxt="";
}

function getSelectedType(){
  $("#search_suggestType").hide();
  var v = $("#selectType").val();
  if ( !v || v == '' ) return ;
  var ot=top.getTypeById( v );
  if ( ot.type_level == 1 ) {
    $("#"+currentTypeId1).css("color", "blue");
    currentTypeId1=v;
    currentTypeId2="";
    currentTypeId3="";
  } else if ( ot.type_level == 2 ) {
    $("#"+currentTypeId1).css("color", "blue");
	$("#"+currentTypeId2).css("color", "blue");
    currentTypeId1=ot.parent_id;
    currentTypeId2=v;
    currentTypeId3="";
  } else {
    $("#"+currentTypeId1).css("color", "blue");
	$("#"+currentTypeId2).css("color", "blue");
	$("#"+currentTypeId3).css("color", "blue");
    currentTypeId1=ot.pp_id;
    currentTypeId2=ot.parent_id;
    currentTypeId3=v;
  }
  reDrawType(1);
  reDrawType(2);
  reDrawType(3);
  doQuery();
  $("#searchTypeTxt").val("输入分类名称的拼音缩写");
  oldSearchTypeTxt="";
}

function keypress(e,t){
    if(e.keyCode==13){
        if(t==1){
          getSelectedGoods();
        }else if(t==2){
          getSelectedType();
        }
    }else return false;
}
function keydown(e,t){
     if(e.keyCode==8){
       if(t==1){
         document.getElementById("searchGoodsTxt").focus();
       }else {
         document.getElementById("searchTypeTxt").focus();
       }
     }
     return false;
 }