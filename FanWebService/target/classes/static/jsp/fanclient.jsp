var loaded = false;

<!-- adds the about text and the Close button behavior -->
	$(document).ready(function(){
 	  $('#divabouttext').load('./text/about.txt');
      $('button').on('click',function(){
        if( !$('#btn-close').length ) {
          var r= $('<button id="btn-close" class="btn-class" onclick="$(&quot;#divabouttext&quot;).fadeOut()">Close</button>');
          $('#divabouttext').append(r);
        }
      });
	});

<!-- gets all the values from the fan web service and displays them on the screen. this is performed once when the body is loaded	 -->
	function getInitialFanValues() {
		if (!loaded) {
			getJsonValue('upstairs-temp','getupstairstemp');
			getJsonValue('downstairs-temp','getdownstairstemp');
			getJsonValue('trigger-diff','gettriggerdiff');
			getJsonValue('fan-setting','getfansetting');
			getJsonValue('fan-status','getstatus');			
 	    	setInterval(getIntervalValues, 1000);  <!--set a timer to run getIntervalValues once a second -->
			loaded = true;
		}
	}
	
<!--  Gets the upstairsTemp and fanstatus values from the web service and displays them on the screen -->
	function getIntervalValues() {
	    getJsonValue('upstairs-temp','getupstairstemp');
		getJsonValue('fan-status','getstatus');		
	}
	
<!-- 	Posts the triggerDiff element in the fan to the web service -->
	function setTriggerDiff() {
		var field = 'trigger-diff';	
    	var d = $('#' + field);
    	var obj = {};
    	obj[field] = d.val();
    	
	    $.ajax({
	        type: 'post',
	        url: 'http://localhost:8080/posttriggerdiff',
	        contentType: 'application/json; charset=utf-8',
	        datatype: 'json',
	        data: JSON.stringify(obj),
	        success: function (html) {},
	        error: function(){ 
	        	console.log('posttriggerdiff POST failed!'); 
	        }
	    });
	}
	
<!-- 	posts the fanSetting element in the fan to the web service. also displays the value ON, OFF, or AUTO. occurs when the ON/OFF/AUTO buttons are clicked -->
	function setFan(f) {
	
		var field = 'fan-setting';
    	var obj = {};
    	obj[field] = f;
		var d = $('#fan-setting');

		if (f=='ON' && f==d.val()) {
			return; <!--  fan is already ON, do nothing -->
		} else if (f=='OFF' && f==d.val()) {
			return; <!-- fan is already OFF, do nothing -->
		} else if (f=="AUTO" && f==d.val()) {
			return; <!-- fan is already AUTO, do nothing -->
		}

		//tell the web service to turn the fan on or off
	    $.ajax({
	        type: 'post',
	        url: 'http://localhost:8080/postfansetting',
	        contentType: 'application/json; charset=utf-8',
	        datatype: 'json',
	        data: JSON.stringify(obj),
	        success: function (html) {
	       		d.val(f);
	        },
	        error: function(){ 
				console.log('fansetting POST failed!'); 
	        }
	    });
	}

<!-- gets the JSON value from the web service -->
	function getJsonValue(element,getMapping) {
		$.ajax({
		    type: 'get',
		    url: 'http://localhost:8080/' + getMapping,
		    dataType: 'json',
	        success: function (data) {
	        	var d = $('#' + element);
	        	if (d.attr('id') == 'fan-status') {
		        	if (data[element]) {
		        		d.val('ON');	
		        	} else {
		        		d.val('OFF');
		        	}	
		        	animateFan(data[element]);
	        	} else {
					d.val(data[element]);
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
			$('#img-fan-off').css('visibility','hidden');
    		$('#img-fan-on').css('visibility','visible');
		} else {
    		$('#img-fan-off').css('visibility','visible');
			$('#img-fan-on').css('visibility','hidden');    		
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

<!-- on keypress event -->
	function validateNoValue() {
		setTriggerDiffColor('#b0b000');
	}

<!-- on blue event	 -->
	function setOnBlur() {
		setTriggerDiffColor('black');
	}
	
	function setOnFocus() {
		$('#trigger-diff').css('box-shadow','inset 0 0 7px #b0b000');
	}
	
<!-- sets the appropriate color for the trigger differential input box -->
	function setTriggerDiffColor(clr) {
		var d = $('#trigger-diff');
		if (d.val().length==0 || d.val()=='0') {
			d.css('box-shadow','inset 0 0 7px red');
		} else {
			d.css('box-shadow','inset 0 0 5px '+clr);
		}
	}
