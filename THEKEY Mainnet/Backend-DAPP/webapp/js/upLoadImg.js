/**
 * Created by Administrator on 2016/10/31.
 */
var d=new Date();
var timeStamp=d.getTime();
var realName="";
var currentFileName="";

function putFile(browserImgId,update){
    var uf=new qq.FileUploader({
        element: document.getElementById(browserImgId),
        action: top.getUrlBase()+"/uploadFile",
        debug: false,
        disableDefaultDropzone: false,
        extraDropzones: [qq.getByClass(document, 'qq-upload-extra-drop-area')[0]],
        onProgress: function(id, fileName, loaded, total){
            setUploadFilePercentById( browserImgId, loaded, total );
        },
        onComplete:function(id, fileName, responseJSON){
            try{
                setUploadFileStatusById( browserImgId,responseJSON.success,fileName);
                update(fileName);
                if ( !responseJSON.success ) {
                    var msg="";
                    if ( responseJSON.error == 'Exist' ) msg='文件已存在，请修改文件名后再上传。';
                    if ( msg != '' ) top.showInfoWinWarn("文件上传失败:"+msg);
                }
            }catch(e){}
        },
        onError: function(id, fileName, reason) {
            try{
                setUploadFileStatusById( browserImgId,responseJSON.success,fileName);
            }catch(e){}
        }
    });

}
function putImg(browserImgId,update){
    var uf=new qq.FileUploader({
        element: document.getElementById(browserImgId),
        action: top.getUrlBase()+"/uploadFile",
        debug: false,
        disableDefaultDropzone: false,
        allowedExtensions:["jpeg","png","gif",'jpg','bmp','jpe','rgb'],
        extraDropzones: [qq.getByClass(document, 'qq-upload-extra-drop-area')[0]],
        onProgress: function(id, fileName, loaded, total){
            setUploadFilePercentById( browserImgId, loaded, total );
        },
        onComplete:function(id, fileName, responseJSON){
            try{
                setUploadFileStatusById( browserImgId,responseJSON.success,fileName);
                update(fileName);
                if ( !responseJSON.success ) {
                    var msg="";
                    if ( responseJSON.error == 'Exist' ) msg='文件已存在，请修改文件名后再上传。';
                    if ( msg != '' ) top.showInfoWinWarn("文件上传失败:"+msg);
                }
            }catch(e){}
        },
        onError: function(id, fileName, reason) {
            try{
                setUploadFileStatusById( browserImgId,responseJSON.success,fileName);
            }catch(e){}
        }
    });

}

function getFileName( fileName ) {
    currentFileName =timeStamp+"_"+fileName;
    //var suffix = fileName.slice(fileName.lastIndexOf('.'));
    //
    //currentFileName = timeStamp+suffix;
    realName = fileName;
    return currentFileName;
}

function setUploadFileStatusById(id,isOK,file_name){
    if ( isOK ) {
        isSaveOK=true;
        try{
            //parent.closeDialog();
        }catch(e){}
    }
}

function setUploadFilePercentById( id, loaded, total ){
    $("#"+id).next("span").html("进度："+ Math.round(loaded*100/total)+'%');
}


