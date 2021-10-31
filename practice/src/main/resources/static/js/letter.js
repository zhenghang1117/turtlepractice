$(function(){
	$("#sendBtn").click(send_letter);
	$(".close").click(delete_msg);
});

function send_letter() {
    $("#sendModal").modal("hide");
    var username = $("#recipient-name").val();
    var content = $("#message-text").val();
    if(!username){
    alert("请填写对方名称!");
    return false;
    }
    if(!content){
    alert("发送内容不能为空!");
    return false;
    }
    $.post(
    CONTEXT_PATH+"/letter/send",
    {"username":username,"content":content},
    function(data){
        data = $.parseJSON(data);
        if(data.code == 0){
          $("#hintBody").text("发送成功!");
         }else{
          $("#hintBody").text(data.msg);
         }
    	$("#hintModal").modal("show");
    	setTimeout(function(){
    		$("#hintModal").modal("hide");
    		location.reload();
    	}, 2000);
    }


    );


}

function delete_msg() {
	// TODO 删除数据
	$(this).parents(".media").remove();
}