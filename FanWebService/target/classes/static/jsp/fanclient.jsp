var loaded = false;

<!-- adds the about text and the Close button behavior -->
	$(document).ready(function(){
 	  $("#divAboutText").load("./text/about.txt");
      $('button').on('click',function(){
        if( !$('#btnClose').length ) {
          var r= $('<button id="btnClose" style="float: right; margin-right: 10px; margin-top: 15px;" onclick="$(&quot;#divAboutText&quot;).fadeOut()">Close</button>');
          $("#divAboutText").append(r);
        }
      });
	});

<!-- gets all the values from the fan web service and displays them on the screen. this is performed once when the body is loaded	 -->
	function getInitialFanValues() {
		if (!loaded) {
			getFanValue('upstairsTemp','getupstairstemp');
			getFanValue('downstairsTemp','getdownstairstemp');
			getFanValue('triggerDiff','gettriggerdiff');
			getFanValue('fansetting','getfansetting');
			getFanValue('fanstatus','getstatus');			
	    	setInterval(getIntervalValues, 1000); <!--  set a timer to run getIntervalValues once a second -->
			loaded = true;
		}
	}
	
<!--  Gets the upstairsTemp and fanstatus values from the web service and displays them on the screen -->
	function getIntervalValues() {
		getFanValue('upstairsTemp','getupstairstemp');
		getFanValue('fanstatus','getstatus');		
	}
	
<!-- 	Posts the triggerDiff element in the fan to the web service -->
	function setTriggerDiff() {
    	var d = document.getElementById("triggerDiff");
	    $.ajax({
	        type: "POST",
	        url: "http://localhost:8080/posttriggerdiff?triggerDiff="+d.value,
	        contentType: "text/plain",
	        datatype: "text",
	        success: function (html) {
	        },
	        error: function(){ 
	          console.log('posttriggerdiff POST failed!'); 
	        }
	    });
	}
	
<!-- 	posts the fanSetting element in the fan to the web service. also displays the value ON, OFF, or AUTO. occurs when the ON/OFF/AUTO buttons are clicked -->
	function setFan(f) {
		var d = document.getElementById("fansetting");
		if (f=='ON' && f==d.value) {
			return; //fan is already ON, do nothing
		} else if (f=='OFF' && f==d.value) {
			return; //fan is already OFF, do nothing
		} else if (f=="AUTO" && f==d.value) {
			return; //fan is already AUTO, do nothing
		} 
		//tell the web service to turn the fan on or off
	    $.ajax({
	        type: "POST",
	        url: "http://localhost:8080/postfansetting?fanSetting="+f,
	        contentType: "text/plain",
	        datatype: "text",
	        success: function (html) {
	       		d.value=f;
	        },
	        error: function(){ 
	          console.log('fansetting POST failed!'); 
	        }
	    });
	}

<!-- get the fan element value as specified by 'element' and 'getMapping' from the web service-->
	function getFanValue(element,getMapping) {
	    $.ajax({
	        type: "GET",
	        url: "http://localhost:8080/"+getMapping,
	        contentType: "text/plain",
	        datatype: "text",
	        success: function (data) {
	        	var d = document.getElementById(element);
	        	if (d.id == "fanstatus") {
		        	if (data) {
		        		d.value="ON";	
						animateFan(true);
		        	} else {
		        		d.value="OFF";
		        		animateFan(false);
		        	}	
	        	} else {
	        		d.value=data;
	        	}
	        },
	        error: function(){ 
	          console.log(getMapping+' GET failed!'); 
	        }
	    });
	}
	
<!-- 	shows the animated or still gif as determined by b, true=animated fan, false=still fan -->
	function animateFan(b) {
		if (b) {
			document.getElementById("fanoff").style.visibility = "hidden";
    		document.getElementById("fanon").style.visibility = "visible";
		} else {
    		document.getElementById("fanoff").style.visibility = "visible";
    		document.getElementById("fanon").style.visibility = "hidden";
		}
	}
	
<!-- 	allows only numeric keys. this is for the triggerDiff input -->
    function onlyNumberKey(evt) {
        // Only ASCII character in that range allowed
        var ASCIICode = (evt.which) ? evt.which : evt.keyCode
        if (ASCIICode > 31 && (ASCIICode < 48 || ASCIICode > 57))
            return false;
        return true;
    } 

	