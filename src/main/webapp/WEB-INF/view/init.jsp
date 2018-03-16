<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="/WEB-INF/tlds/base.tld" prefix="cn" %>

<html>
<head>
    <title>Title</title>
    <cn:base/>
    <link href="public/plugin/bootstrap3.3.7/css/bootstrap.min.css" rel="stylesheet" type="text/css"/>
</head>
<body>
<a href="javascript:addPanel()" class="glyphicon glyphicon-plus"></a>
<button type="button" class="btn btn-primary">提交</button>
<div id="body">
      <span class="panel col-xs-12 col-sm-6 col-md-4 col-lg-3">
           <div class="form-group">
            <label>手机号</label>
            <input type="number" class="form-control" name="phone" placeholder="手机号码"/>
          </div>
          <div class="form-group">
            <label>name</label>
            <input type="text" class="form-control" name="name" placeholder="姓名"/>
          </div>
      </span>
</div>
<script src="public/plugin/jquery-3.3.1.min.js"></script>
<script src="public/plugin/bootstrap3.3.7/js/bootstrap.min.js"></script>
<script>
    var temp = $('.panel').html();
    function addPanel() {
        $('#body').append(temp);
    }
    $('.btn').on('click', function () {
        var array = new Array();
        $('.panel').each(function () {
            var data = {};
            data.phone = this.find("input[name=phone]").val();
            data.name = this.find("input[name=name]").val();
            array.push(data);
        })
        if (array.length > 0) {
            $.ajax({
                url: 'common/doInit',
                type: 'post',
                data: 'vo=' + array,
                dataType: 'json',
                success: function (res) {
                    if (res.code == 0) {
                        alert('新增顶级会员成功!');
                    } else {
                        alert('新增顶级会员失败!');
                    }
                }
            })
        }
    });
</script>
</body>
</html>
