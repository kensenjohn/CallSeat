/* FORMAT PHONE BEHAVIOR MASK */
function FormatPhone (e,input) {
    /* to prevent backspace, enter and other keys from
     interfering w mask code apply by attribute
     onkeydown=FormatPhone(control)
     */
    evt = e || window.event; /* firefox uses reserved object e for event */
    var pressedkey = evt.which || evt.keyCode;
    var BlockedKeyCodes = new Array(8,27,13,9); //8 is backspace key
    var len = BlockedKeyCodes.length;
    var block = false;
    var str = '';
    for (i=0; i<len; i++){
        str=BlockedKeyCodes[i].toString();
        if (str.indexOf(pressedkey) >=0 ) block=true;
    }
    if (block) return true;

    s = input.value;
    if (s.charAt(0) =='+') return false;
    filteredValues = '"`!@#$%^&*()_+|~-=\QWERT YUIOP{}ASDFGHJKL:ZXCVBNM<>?qwertyuiop[]asdfghjkl;zxcvbnm,./\\\'';
    var i;
    var returnString = '';

    /* Search through string and append to unfiltered values
     to returnString. */

    for (i = 0; i < s.length; i++) {
        var c = s.charAt(i);

        //11-Digit number format if leading number is 1

        if (s.charAt(0) == 1){
            if ((filteredValues.indexOf(c) == -1) & (returnString.length <  14 )) {
                if (returnString.length==1) returnString +='(';
                if (returnString.length==5) returnString +=')';
                if (returnString.length==6) returnString +=' ';
                if (returnString.length==10) returnString +='-';
                returnString += c;
            }
        }

        //10-digit number format
        else{
            if ((filteredValues.indexOf(c) == -1) & (returnString.length <  13 )) {
                if (returnString.length==0) returnString +='(';
                if (returnString.length==4) returnString +=')';
                if (returnString.length==5) returnString +=' ';
                if (returnString.length==9) returnString +='-';
                returnString += c;
            }
        }

    }
    input.value = returnString;

    return false}