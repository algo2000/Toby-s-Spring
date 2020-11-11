var oldVal = "";
$("#text").on("change keyup paste", function() {
    var currentVal = $(this).val();
    console.log(currentVal);
    if(currentVal == oldVal) {
        return;
    }

    $.ajax({
        url: "/test",
        type: "POST",
        dataType: "text",
        success: function(result){
            console.log(result);
        }
    })

    oldVal = currentVal;
});
