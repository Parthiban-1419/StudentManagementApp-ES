import Route from '@ember/routing/route';
import { service } from '@ember/service';

export default class IndexRoute extends Route {
  @service store;

  model() {
    window.location =
      'http://localhost:8080/StudentManagementApp/secured/index.jsp';
  }

  //model(params) {
  // const { role } = params;
  // this.store.role = role;
  // let self = this;
  // var req = new XMLHttpRequest();

  // req.onload = function () {
  //   console.log(this.responseText);
  //   self.marks = JSON.parse(this.responseText).marks;
  //   self.students = JSON.parse(this.responseText).students;
  // };

  // req.open('POST', 'http://localhost:8080/Student_Management/get-marks', false);
  // req.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
  // req.send();

  // return self.marks;
  //}
}
