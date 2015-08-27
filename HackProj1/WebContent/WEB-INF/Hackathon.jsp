<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<meta content="no-cache" http-equiv="pragma"></meta>
<meta content="no-cache" http-equiv="cache-control"></meta>
<meta content="0" http-equiv="expires"></meta>
<meta content="IE=9" http-equiv="X-UA-Compatible"></meta>


<link href="CommonCSS.css" rel="stylesheet" type="text/css" />
<script src="jquery-1.11.3.js"></script>


<title>Hackathon</title>

<!-- <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script> -->

<script type="text/javascript">
	$(document).ready(function() {
		var uid = $('#userId1').val();
		$('#reset').click(function(e) {
			e.defaultPrevented;
			clearOnSave();
		});
		$('#save').click(function(e) {
			$("#billDetail tbody").empty();
			alert("check");
			$.ajax({
				type : 'GET',
				url : 'http://localhost:9090/WS-V1/rest/gadget',
				data : {
	                userId : $('#userId1').val(),
					location : $('#location').val(), 
					deviceCategory : $('#deviceCategory').val(),
					dataVolume : $('#dataVolume').val(),
	            },
				contentType : 'application/json; charset=utf-8',
				dataType : 'json',
				success : OnGetMemberSuccess,
				error : OnGetMemberError
			});
			
		$('#show_bill').click(function(e) {
			$("#billDetail tbody").empty();
			$.ajax({
				type : 'GET',
				url : 'http://localhost:9090/WS-V1/rest/currentBill',
				data : {
	                userId : $('#userId1').val()
	            },
				contentType : 'application/json; charset=utf-8',
				dataType : 'json',
				success : OnGetMemberSuccess,
				error : OnGetMemberError
			});
		});

	});

	function OnGetMemberSuccess(data, status) {
		alert("sucess");
		$.each(data, function(i, obj) {
			$("#billDetail tbody").append(
					"<tr class=\"trow\">" + "<td>" + obj.deviceCategory
							+ "</td>" + "<td>" + obj.dataVolume + "</td>"
							+ "<td>" + obj.currentBillAmt + "</td>" + "</tr>");
		});
		$("#billDetail tbody tr:even").css("background-color", "#E0E0E0");
		//jQuery code will go here...
	}

	function OnGetMemberError(request, status, error) {
		//jQuery code will go here...
		alert("failure");
	}

	function clearOnSave() {
		$("#userId").val("");
		$("#location").val("");
		$("#deviceCategory").val("");
		$("#dataVolume").val("");
	}
</script>
</head>
<body>
	<p id="header">Hackathon Testing</p>

	<table align="center">
		<tr>

			<td class="tdata">
				<form action="http://10.76.134.161:9090/WS-V1/rest/gadget">
					<table border="1">
						<thead class="theader">
							<tr>
								<th colspan="2">Mock Data</th>
							</tr>
						</thead>

						<tr>
							<td>User Id</td>
							<td><input type="text" name="userId" id="userId"></td>
						</tr>
						<tr>
							<td>Location</td>
							<td><input type="text" name="location" id="location"></td>
						</tr>
						<tr>
							<td>Data Category</td>
							<td><input type="text" name="deviceCategory"
								id="deviceCategory"></td>
						</tr>
						<tr>
							<td>Data Volume</td>
							<td><input type="text" name="dataVolume" id="dataVolume"></td>
						</tr>
						<tr>
							<td align="right"><input type="submit" value="Save"></td>
							<td><input type="button" value="Reset" id="reset"></td>

						</tr>
					</table>
				</form>
			</td>

			<td class="tdata">
				<form>
					<table border="1">
						<tr>
							<td>User Id</td>
							<td><input type="text" name="userId1" id="userId1"></td>
							<td><input type="button" value="Show Bill" id="show_bill"></td>
						</tr>
					</table>
				</form>
			</td>


			<td class="tdata">
				<table border="1" id="billDetail">
					<thead class="theader">
						<tr>
							<th>Device Category ID</th>
							<th>Data Volume (MB)</th>
							<th>Amount ($)</th>
						</tr>
					</thead>
					<tbody>

					</tbody>
				</table>
			</td>


		</tr>
	</table>

</body>
</html>