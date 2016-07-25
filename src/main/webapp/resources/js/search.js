$(document).ready(function() {
    $('#search_field').focus();

    $('#search_field').keypress(function(e){
        if(e.which == 13){//Enter key pressed
            $('#search_button').click();//Trigger search button click event
        }
    });
});