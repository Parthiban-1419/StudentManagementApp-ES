import EmberRouter from '@ember/routing/router';
import config from 'mark-management/config/environment';

export default class Router extends EmberRouter {
  location = config.locationType;
  rootURL = config.rootURL;
}

Router.map(function () {
  this.route('add-student');
  this.route('add-users');
  this.route('secured', function () {
    this.route('app');
  });
  this.route('MarkManagement');
  this.route('home', { path: '/StudentManagementApp//:role' });
  this.route('view-log');
});
