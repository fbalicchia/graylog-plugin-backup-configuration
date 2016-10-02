import Reflux from 'reflux';


import BackupActions from 'components/BackupActions'


import UserNotification from 'util/UserNotification';
import URLUtils from 'util/URLUtils';
import fetch from 'logic/rest/FetchProvider';

const urlPrefix = '/plugins/org.graylog.plugins.backup';

const BackupStore = Reflux.createStore({
    listenables: [BackupActions],

    getInitialState() {
        return {
            config: undefined,
        };
    },

    _errorHandler(message, title, cb) {
        return (error) => {
            let errorMessage;
            try {
                errorMessage = error.additional.body.message;
            } catch (e) {
                errorMessage = error.message;
            }
            UserNotification.error(`${message}: ${errorMessage}`, title);
            if (cb) {
                cb(error);
            }
        };
    },

    _url(path) {
        return URLUtils.qualifyUrl(`${urlPrefix}${path}`);
    },


    launchRestore() {
        console.log("restore chiamato");
        const promise = fetch('POST', this._url('/launchrestore'));
        promise.then((response) => {
            this.trigger({ config: response });
            UserNotification.success('Configuration restore was completed successfully');
        }, this._errorHandler('Backup restore failed', 'Unable restore config'));

        BackupActions.launchRestore.promise(promise);
    },


});

export default BackupStore;