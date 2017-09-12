<html>
<head>

<title>Schedule to Reddit</title>
<link rel="shortcut icon" type="image/png" th:href="@{/resources/favicon.png}"/>

<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/css/bootstrap.min.css"/>
</head>
<body>
<div th:include="header"/>

<div class="container">
<h1>My RSS Feeds</h1>
<br/>
<a th:href="@{/feedForm}" class="btn btn-primary">Add New RSS Feed</a>
<br/>
<br/>
<table class="table table-bordered">
<thead>
<tr>
<th>Name</th>
<th>Feed URL</th>
<th>Actions</th>
</tr>
</thead>

</table>
</div>
<script src="https://cdn.datatables.net/1.10.7/js/jquery.dataTables.min.js"></script>
<script src="https://cdn.datatables.net/plug-ins/1.10.7/integration/bootstrap/3/dataTables.bootstrap.js"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/js/bootstrap.min.js"></script>

<script>
/*<![CDATA[*/
           
$(document).ready(function() {
    $('table').dataTable( {
        "processing": true,
        "searching":false,
        "columnDefs": [
                       { "name": "name",   "targets": 0},
                       { "name": "url",  "targets": 1},
                       { "targets": 2, "data": "id",
                    	    "render": function ( data, type, full, meta ) {
                    	        return '<a href="#" class="btn btn-danger" onclick="confirmDelete('+data
                                +') ">Delete</a>';
                    	      }}
                     ],
                     "columns": [
                                 { "data": "name" },
                                 { "data": "url" }
                             ],
        "serverSide": true,
        "ajax": function(data, callback, settings) {
            $.get('api/myFeeds', {
                size: data.length,
                page: (data.start/data.length),
                sortDir: data.order[0].dir,
                sort: data.columns[data.order[0].column].name
            }, function(res,textStatus, request) {
            	var pagingInfo = request.getResponseHeader('PAGING_INFO');
            	var total = pagingInfo.split(",")[0].split("=")[1];
                callback({
                    recordsTotal: total,
                    recordsFiltered: total,
                    data: res
                });
            });
        }
    } );
} );           
           

function confirmDelete(id) {
    if (confirm("Do you really want to delete this site?") == true) {
        deleteSite(id);
    } 
}

function deleteSite(id){
    $.ajax({
        url: 'api/myFeeds/'+id,
        type: 'DELETE',
        success: function(result) {
            window.location.href="feeds"
        }
    });
}
/*]]>*/
</script>
</body>
</html>