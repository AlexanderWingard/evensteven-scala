var options; // globally available
$(document).ready(function() {
    options = {
         chart: {
            renderTo: 'evenchart',
            defaultSeriesType: 'column'
         },
         title: {
            text: 'EvenSteven'
         },
	xAxis: {
	    categories: []
	},
	series: []
      };
   });

function callSendEven() {
    sendEven("sendEvenCallback", [$("#eventname").html(), $("#input").val()])
}

function sendEvenCallback(result) {
    var names = [];
    var sums = [];
    $.each($.parseJSON(result), function(name, sum) {
	names.push(name);
	sums.push(sum);
    });
    options.xAxis.categories = names;
    options.series = [{
	data : sums
    }];
    new Highcharts.Chart(options);
}