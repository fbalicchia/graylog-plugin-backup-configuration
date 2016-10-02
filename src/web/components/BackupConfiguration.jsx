import React from 'react';
import { Input, Button } from 'react-bootstrap';
import BootstrapModalForm from 'components/bootstrap/BootstrapModalForm';
import { IfPermitted, Select, ISODurationInput } from 'components/common';
import ObjectUtils from 'util/ObjectUtils';
import BackupActions from "components/BackupActions";
import BackupStore from "components/BackupStore";

const BackupConfigurationInfo = React.createClass({
  propTypes: {
    config: React.PropTypes.object,
    updateConfig: React.PropTypes.func.isRequired,
  },


  getDefaultProps() {
    return {
      config: {
        enabled: false,
        cfg_backup_path: 'data/bck',
        cfg_restore_path: 'data/restore',
        mongodump_path: '/usr/bin',
        scheduled_period: 'P15D',
      },
    };
  },

getInitialState() {
    return {
      config: ObjectUtils.clone(this.props.config),
    };
  },

  componentWillReceiveProps(newProps) {
    this.setState({config: ObjectUtils.clone(newProps.config)});
  },

  _updateConfigField(field, value) {
    const update = ObjectUtils.clone(this.state.config);
    update[field] = value;
    this.setState({config: update});
  },

  _onCheckboxClick(field, ref) {
    return () => {
      this._updateConfigField(field, this.refs[ref].getChecked());
    };
  },

  _onSelect(field) {
    return (selection) => {
      this._updateConfigField(field, selection);
    };
  },

  _onUpdate(field) {
    return (e) => {
      this._updateConfigField(field, e.target.value);
    };
  },

  _onUpdatePeriod(field) {
    return (value) => {
      const update = ObjectUtils.clone(this.state.config);
      update[field] = value;
      this.setState({ config: update });
    };
  },


  _expirationThresholdValidator(milliseconds) {
    return milliseconds >= 60 * 1000;
  },

  _openModal() {
    this.refs.backupConfigModal.open();
  },

  _closeModal() {
    this.refs.backupConfigModal.close();
  },

  _launchRestore() {
    BackupActions.launchRestore();
  },

  _resetConfig() {
    // Reset to initial state when the modal is closed without saving.
    this.setState(this.getInitialState());
  },

  _saveConfig() {
    this.props.updateConfig(this.state.config).then(() => {
      this._closeModal();
    });
  },
  
  render() {
    return (
        <div>
          <h3>Backup configuration</h3>
          <p>
            Graylog backup plugin for dump Graylog configuration data.
            With <strong>Update Configuration</strong> is possible to adapt backup configuration to your needs and system. <strong>Restore data</strong> button restore data under directory Backuprestore/graylog
          </p>
          <dl className="deflist">
            <dt>Enabled:</dt>
            <dd>{this.state.config.enabled === true ? 'yes' : 'no'}</dd>
            <dt>Backup frequency:</dt>
            <dd>{this.state.config.scheduled_period}</dd>
            <dt>BackupPath:</dt>
            <dd>{this.state.config.cfg_backup_path}</dd>
            <dt>Backuprestore:</dt>
            <dd>{this.state.config.cfg_restore_path}</dd>
            <dt>Mongodump path:</dt>
            <dd>{this.state.config.mongodump_path}</dd>
          </dl>

          <IfPermitted permissions="clusterconfigentry:edit">
            <Button bsStyle="info" bsSize="xs" onClick={this._openModal}>Update Configuration</Button>
            <Button bsStyle="info" bsSize="xs" onClick={this._launchRestore}>Restore Data</Button>
          </IfPermitted>

          <BootstrapModalForm ref="backupConfigModal"
                              title="Update Graylog backup Configuration"
                              onSubmitForm={this._saveConfig}
                              onModalClose={this._resetConfig}
                              submitButtonText="Save">
            <fieldset>
              <Input type="checkbox"
                       ref="configEnabled"
                     label="Enable Backup process"
                     name="enabled"
                     checked={this.state.config.enabled}
                     onChange={this._onCheckboxClick('enabled', 'configEnabled')}/>

              <Input type="text"
                     label="Bakup directory"
                     name="backup_dir"
                     value={this.state.config.cfg_backup_path}
                     onChange={this._onUpdate('cfg_backup_path')}/>

              <Input type="text"
                     label="Restore directory"
                     name="backup_dir"
                     value={this.state.config.cfg_restore_path}
                     onChange={this._onUpdate('cfg_restore_path')}/>

              <Input type="text"
                     label="Path where mongodump is installed"
                     name="mongodump_dir"
                     value={this.state.config.mongodump_path}
                     onChange={this._onUpdate('mongodump_path')}/>

              <ISODurationInput duration={this.state.config.scheduled_period}
                                update={this._onUpdatePeriod('scheduled_period')}
                                label="Expiration threshold (as ISO8601 Duration)"
                                help="Amount of time after which inactive collectors are purged from the database."
                                validator={this._expirationThresholdValidator}
                                errorText="invalid (min: 1 minute)"
                                required />
            </fieldset>
          </BootstrapModalForm>
        </div>
    );
  },
});

export default BackupConfigurationInfo;
