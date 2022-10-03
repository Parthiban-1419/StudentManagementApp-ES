import Route from '@ember/routing/route';
import { tracked } from '@glimmer/tracking';
import { service } from '@ember/service';

export default class ViewLogRoute extends Route {
  @tracked jArray;
  @service store;
  @service router;

  model() {
    let self = this;
    if(self.store.role != 'CEO'){
        self.router.transitionTo('index');
    }
    var req = new XMLHttpRequest();
    req.onload = function () {
      console.log(this.responseText);
      try {
        self.jArray = JSON.parse(this.responseText);
        console.log(self.jArray);
      } catch (e) {
        alert('Error while fetching logs');
      }
    };
    req.open('POST', 'http://localhost:8080/StudentManagementApp/logs', false);
    req.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
    req.send('index=crud-log&field=operation&query=*');

    return self.jArray;
  }

  setupController = function(controller) {
    console.log("setUpController");
    console.log(controller.data);
    console.log(this.jArray);
    controller.set('data', this.jArray);
    console.log(controller.data);

  }
} //'[{"action":"get"}]'

