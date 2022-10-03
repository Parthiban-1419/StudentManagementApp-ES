import Controller from '@ember/controller';
import { action } from '@ember/object';
import { tracked } from '@glimmer/tracking';
import { service } from '@ember/service';

export default class HomeController extends Controller {
  @tracked edited = false;
  @tracked row = 0;
  @service store;

  @action
  deleteRow(reg_number) {
    if (confirm('Click ok to delete') == true) {
      $('#' + reg_number).remove();
      this.deleteDB(reg_number);
    }
  }

  @action
  editRow(reg_number) {
    let self = this;
    $('#' + reg_number + ' th center input').removeAttr('disabled');
    self.edited = true;
    console.log('editing...');
  }

  deleteDB(reg_number) {
    let self = this;
    var req = new XMLHttpRequest();

    req.open(
      'POST',
      'http://localhost:8080/StudentManagementApp/delete-marks',
      true
    );
    req.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
    req.send('user=' + self.store.user + '&regNumber=' + reg_number + '&index=crud-log');
  }

  @action
  updateRows() {
    var req = new XMLHttpRequest(),
      self = this,
      regNumber,
      jLog = [],
      json,
      jArray = [];
    var tr = document.getElementsByTagName('tr');
    let j = 0;
    for (let i = 1; i < tr.length; i++) {
      if ($(tr[i]).attr('modified') === 'false') continue;

      if ($(tr[i]).find('#regNumber').val() === '')
        regNumber = $(tr[i]).find('#regNumber').text();
      else regNumber = $(tr[i]).find('#regNumber').val();

      // jLog[j]={
      //   userId : self.store.user,
      //   operation : 'UPDATE',
      //   studentId : parseInt(regNumber),
      //   time : Date.now()
      // }
      json = {
        regNumber: parseInt(regNumber),
        tamil: parseInt($(tr[i]).find('#tamil').val()),
        english: parseInt($(tr[i]).find('#english').val()),
        maths: parseInt($(tr[i]).find('#maths').val()),
        science: parseInt($(tr[i]).find('#science').val()),
        social: parseInt($(tr[i]).find('#social').val()),
        total: parseInt($(tr[i]).find('#total').text()),
        percentage: parseInt($(tr[i]).find('#percentage').text()),
      };
      jArray[j++] = json;
    }

    req.onload = function () {
      console.log(this.responseText);
      if (this.responseText === 'true') {
        // self.storeLog(jLog);
        console.log(JSON.stringify(jArray));
        alert('Successfully updated');
        location.reload();
      } else {
        console.log(this.responseText);
        alert('Somethimg went wrong. Check the reg number');
      }
    };

    req.open(
      'POST',
      'http://localhost:8080/StudentManagementApp/update-marks',
      true
    );
    req.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
    req.send(
      'user=' +
        self.store.user +
        '&jArray=' +
        JSON.stringify(jArray) +
        '&time=' +
        Date.now()  +
        '&index=crud-log'

    );
  }

  @action
  addRow() {
    let self = this;
    var tags =
      '<tr id="newRow" modified="true">' +
      '<th><center><input type="number" id="regNumber" ></center></th>' +
      '<th><center><input type="number" id="tamil" value=0  min="0" max="100"></center></th>' +
      '<th><center><input type="number" id="english" value=0  min="0" max="100"></center></th>' +
      '<th><center><input type="number" id="maths" value=0  min="0" max="100"></center></th>' +
      '<th><center><input type="number" id="science" value=0  min="0" max="100"></center></th>' +
      '<th><center><input type="number" id="social" value=0  min="0" max="100"></center></th>' +
      '<th><center><span type="number" id="total" >0</span></center></th>' +
      '<th><center><span type="number" id="percentage" >0</span></center></th>' +
      '<td><button><i id="delete" class="fa fa-trash" ></i></button></td>' +
      '</tr>';
    $('table').append(tags);
    self.edited = true;
    // jLog[0]={
    //   userId : self.store.user,
    //   operation : 'CREATE',
    //   studentID : $(document).attr(),
    //   time : Date.now()
    // }

    $('#delete').click(function () {
      if (
        confirm(
          'Changes you made may not be saved.\nAre you sure deleting this row?'
        ) == true
      )
        $(this).parent().parent().parent().remove();
    });

    $('input').change(function () {
      var tr = $(this).parent().parent().parent(),
        tamil,
        english,
        maths,
        science,
        social,
        total,
        percentage;

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
  }

  changed() {
    this.edited = true;
    $('input').change(function () {
      var tr = $(this).parent().parent().parent(),
        tamil,
        english,
        maths,
        science,
        social,
        total,
        percentage;

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
  }

  storeLog(jArray) {
    var req = new XMLHttpRequest();

    req.onload = function () {
      console.log(this.responseText);
    };

    req.open('POST', 'http://localhost:8080/StudentManagementApp/logs', false);
    req.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
    req.send('jArray=' + JSON.stringify(jArray) + '&index=crud-log');
  }
}
