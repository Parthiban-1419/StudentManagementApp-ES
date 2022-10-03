import Controller from '@ember/controller';
import { action } from '@ember/object';
import { service } from '@ember/service';

export default class AddStudentController extends Controller {
  @service router;
  @action
  addStudent() {
    let self = this;
    var req = new XMLHttpRequest();

    var number = $('#number').val();
    var name = $('#name').val();
    var gender = $('#gender').val();
    var dob = $('#dob').val();

    req.onload = function () {
      alert(this.responseText);
      self.router.transitionTo('index');
    };

    req.open(
      'POST',
      'http://localhost:8080/StudentManagementApp/add-student',
      true
    );
    req.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
    req.send(
      'number=' + number + '&name=' + name + '&gender=' + gender + '&dob=' + dob
    );
  }
}
