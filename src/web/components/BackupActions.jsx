import Reflux from 'reflux';

const BackupActions = Reflux.createActions({
    launchRestore: { asyncResult: true },
});

export default BackupActions;