import Controller from '@ember/controller';
import { action } from '@ember/object';
import { tracked } from '@glimmer/tracking';

export default class ViewLogController extends Controller {
    @tracked data;
    @action
    getDate(dateTime){
        var date = new Date(dateTime);
        return date.getDate() + '/' + parseInt(date.getMonth() + 1) + '/' + date.getFullYear();
    }

    getTime(dateTime){
        var time = new Date(dateTime);
        return time.getHours() + ":" + (time.getMinutes() < 10 ? '0' + time.getMinutes() : time.getMinutes()) + ":" + (time.getSeconds() < 10 ? '0' + time.getSeconds() : time.getSeconds());
    }

    @action
    applyFilter(e){
        let self = this;
        var req = new XMLHttpRequest(), value = $('#value').val() + "*";
        req.onload = function () {
            console.log(this.responseText);
            try {
                console.log(self.data);
                self.data = JSON.parse(this.responseText);
                console.log(self.data);
            } catch (e) {
                alert('Error while fetching logs');
            }
        };
        req.open('POST', 'http://localhost:8080/StudentManagementApp/logs', false);
        req.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
        req.send('index=crud-log&field=' + $('#field').val() + '&query=' + $('#value').val());
        console.log('index=crud-log&field=' + $('#field').val() + '&query=' + value);
    }
}
