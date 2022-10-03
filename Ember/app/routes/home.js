import Route from '@ember/routing/route';
import { tracked } from '@glimmer/tracking';
import { service } from '@ember/service';

export default class HomeRoute extends Route {
  @tracked marks;
  @tracked students;

  @service store;
  @service router;

  model(param) {
    let { user } = param.role;
    let self = this;
    var req = new XMLHttpRequest(),
      json,
      jLog;
    console.log(param.role);
    req.onload = function () {
      try {
        console.log(this.responseText);
        json = JSON.parse(this.responseText);
        if (json.user === '') self.router.transitionTo('index');
        self.store.user = param.role;
        self.marks = json.marks;
        self.students = json.students;
        self.getRole(param.role);
      } catch (e) {
        console.log(e);
        self.router.transitionTo('index');
      }
    };

    req.open(
      'POST',
      'http://localhost:8080/StudentManagementApp/get-marks',
      false
    );
    req.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
    req.send('&index=crud-log');

    return self.marks;
  }

  getRole(user){
    let self = this;
    var req = new XMLHttpRequest();

    req.onload = function () {
      let response = this.responseText;
      console.log(this.responseText);
      self.store.role = this.responseText;
      $(document).ready(function () {
        if (response === 'Student') {
          $('tr').hide();
          $('#head').show();
          $('#' + user).show();
          $('td #add').hide();
          $('td #edit').hide();
          $('td #delete').hide();
        } else if (response === 'Staff') {
          $('td #delete').remove();
        } else if (response === 'HOD') {
          $('td #add').hide();
        } else if(response === 'Principal'){
          $('#addStudent').css('display','inline');
        } else if(response === 'CEO'){
          $('td #add').hide();
          $('td #edit').hide();
          $('td #delete').hide();
          $('#logs').css('display','inline');
          $('#addStudent').css('display','inline');
        }

        $('input').change(function () {
          console.log('changed');
          var tr = $(this).parent().parent().parent(),
            tamil,
            english,
            maths,
            science,
            social,
            total,
            percentage;

          $(tr).attr('modified', 'true');

          if ($(this).attr('id') == 'regNumber') {
            tr.attr('id', $(this).val());
          } else {
            if ($(this).val() > 100 || $(this).val() < 0) {
              alert('Mark must be greater than 0 and less than 100');
              $(this).val('0');
            }
            tamil = parseInt(tr.find('#tamil').val());
            english = parseInt(tr.find('#english').val());
            maths = parseInt(tr.find('#maths').val());
            science = parseInt(tr.find('#science').val());
            social = parseInt(tr.find('#social').val());
            total = tamil + english + maths + science + social;
            percentage = total / 5;

            tr.find('#total').text(total);
            tr.find('#percentage').text(percentage);
          }
        });
      });
    };
    req.open('POST', 'http://localhost:8080/StudentManagementApp/get-role', false);
    req.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
    req.send('name=' + user + '&index=crud-log');
  }
}
