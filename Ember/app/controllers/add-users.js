import Controller from '@ember/controller';
import { action } from '@ember/object';
import { service } from '@ember/service';

export default class AddUsersController extends Controller {
  @service router;

  @action
  createAccount() {
    let self = this;
    var req = new XMLHttpRequest();

    var logInName = $('#logInName').val();
    var firstName = $('#firstName').val();
    var middleName = $('#middleName').val();
    var role = $('#role').val();
    var lastName = $('#lastName').val();
    var password = $('#password').val();
    var cPassword = $('#cPassword').val();

    if (password === cPassword) {
      req.onload = function () {
        console.log(this.responseText);
        alert(this.responseText);
        self.router.transitionTo('index');
      };

      req.open(
        'POST',
        'http://localhost:8080/StudentManagementApp/create-account',
        true
      );
      req.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
      req.send(
        'logInName=' +
          logInName +
          '&firstName=' +
          firstName +
          '&middleName=' +
          middleName +
          '&lastName=' +
          lastName +
          '&role=' +
          role +
          '&password=' +
          password
      );
    } else alert('Password miss match');
  }

  checkloginName() {
    var req = new XMLHttpRequest();

    var logInName = $('#logInName').val();
    req.onload = function () {
      console.log(this.responseText);
      if (this.responseText == 'false') {
        if (
          $('#password').val() != '' &&
          $('#password').val() === $('#cPassword').val()
        )
          $('#button').show();
      } else {
        alert('Log in name unavailable');
        $('#button').hide();
      }
    };
    req.open(
      'POST',
      'http://localhost:8080/StudentManagementApp/check-login-name',
      true
    );
    req.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
    req.send('logInName=' + logInName);
  }
}
