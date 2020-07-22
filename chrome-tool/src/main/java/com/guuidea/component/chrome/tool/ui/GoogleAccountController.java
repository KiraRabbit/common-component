package com.guuidea.component.chrome.tool.ui;

import static com.guuidea.component.chrome.tool.common.Constant.EMPTY_STR;

import com.guuidea.component.chrome.tool.common.AesCipher;
import com.guuidea.component.chrome.tool.common.AlertUtils;
import com.guuidea.component.chrome.tool.common.StringUtils;
import com.guuidea.component.chrome.tool.common.UUIDUtils;
import com.guuidea.component.chrome.tool.model.GoogleAccount;
import com.guuidea.component.chrome.tool.model.ProxyAccount;
import com.guuidea.component.chrome.tool.service.GoogleStoreDB;
import com.guuidea.component.chrome.tool.service.ProxyStoreDB;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.StringConverter;

/**
 * 谷歌账号设置控制器
 *
 * @Author: hzchendou
 * @Date: 2019-07-04 07:19
 * @since: 1.0
 * @Copyright (C), 杭州古点科技有限公司
 */
public class GoogleAccountController implements StageLifeCycle {

    @FXML
    private TextField gUserName;

    @FXML
    private TextField gPassword;

    @FXML
    private TextField gLoginUrl;

    @FXML
    private ComboBox gProxySelect;

    @FXML
    private CheckBox gCheckIp;

    /**
     * 当前账户信息
     */
    private GoogleAccount account;

    /**
     * 账号舞台界面
     */
    private Stage accountStage;

    /**
     * 父控制器
     */
    private MainLayoutController parentController;


    /**
     * 初始化
     */
    @Override
    public void init(Stage stage) {
        this.accountStage = stage;
        gProxySelect.setConverter(new StringConverter() {
            @Override
            public String toString(Object object) {
                if (object instanceof ProxyAccount) {
                    return ((ProxyAccount) object).getName();
                }
                return null;
            }

            @Override
            public Object fromString(String string) {
                return null;
            }
        });
    }

    /**
     * 设置账号信息
     *
     * @param account
     */
    public void setGoogleAccount(GoogleAccount account) {
        this.account = account;
        //设置代理信息
        resetProxySelect();
        if (account == null) {
            gUserName.setText(EMPTY_STR);
            gPassword.setText(EMPTY_STR);
            gLoginUrl.setText(EMPTY_STR);
            gCheckIp.setSelected(Boolean.FALSE);
        } else {
            gUserName.setText(account.getUserName());
            gPassword.setText(AesCipher.cipher.decodeToString(account.getPassword()));
            gLoginUrl.setText(account.getLoginUrl());
            gCheckIp.setSelected(account.getCheckIp() == null ? Boolean.FALSE : account.getCheckIp());
            ProxyAccount proxyAccount = ProxyStoreDB.queryById(account.getProxyId());
            gProxySelect.setValue(proxyAccount);
        }

    }

    /**
     * 重置下拉选择框
     */
    private void resetProxySelect() {
        ObservableList<ProxyAccount> ciphers = FXCollections.observableArrayList();
        ciphers.addAll(ProxyStoreDB.loadAllProxyAccount());
        gProxySelect.setItems(ciphers);
    }


    /**
     * 组装成谷歌账号
     *
     * @return
     */
    private GoogleAccount toGoogleAccount() {
        String userName = gUserName.getText();
        if (StringUtils.isBlank(userName)) {
            AlertUtils.showError("账号名称不能为空");
            return null;
        }

        String password = gPassword.getText();
        if (StringUtils.isBlank(password)) {
            AlertUtils.showError("密码信息不能为空");
            return null;
        }

        String loginUrl = gLoginUrl.getText();
        if (StringUtils.isBlank(loginUrl)) {
            AlertUtils.showError("登录地址不能为空");
            return null;
        }

        Boolean checkIp = gCheckIp.isSelected();
        Object val = gProxySelect.getValue();
        if (val == null || !(val instanceof ProxyAccount)) {
            AlertUtils.showError("请选择代理服务");
            return null;
        }
        GoogleAccount account = new GoogleAccount();
        account.setUserName(userName);
        //加密存储
        account.setPassword(AesCipher.cipher.encryptToString(password));
        account.setLoginUrl(loginUrl);
        account.setCheckIp(checkIp);
        account.setProxyId(((ProxyAccount) val).getId());
        return account;
    }

    /**
     * 初始化界面信息
     *
     * @param account
     */
    public void editAccount(GoogleAccount account) {
        setGoogleAccount(account);
        show();
    }

    /**
     * 处理提交事件
     */
    public void handleSubmit() {
        //获取内容
        GoogleAccount account = toGoogleAccount();
        if (account == null) {
            return;
        }
        if (this.account == null) {
            account.setId(UUIDUtils.genUUID());
            account.setCreatedAt(System.currentTimeMillis());
            account.setUpdatedAt(System.currentTimeMillis());
            GoogleStoreDB.addGoogleAccount(account);
            handleCancel();
            parentController.reloadGoogleAccountTable();
            return;
        }
        account.setId(this.account.getId());
        account.setCreatedAt(this.account.getCreatedAt());
        account.setLastLoginAt(this.account.getLastLoginAt());
        account.setPreIp(this.account.getPreIp());
        account.setUpdatedAt(System.currentTimeMillis());
        GoogleStoreDB.upadteGoogleAccount(account);
        handleCancel();
        parentController.reloadGoogleAccountTable();
    }

    /**
     * 处理取消事件
     */
    public void handleCancel() {
        hide();
    }

    /**
     * 显示
     */
    @Override
    public void show() {
        resetProxySelect();
        accountStage.show();
    }


    /**
     * 隐藏
     */
    @Override
    public void hide() {
        setGoogleAccount(null);
        accountStage.hide();
    }

    /**
     * 销毁
     */
    @Override
    public void destroy() {
       hide();
    }

    public void setParentController(MainLayoutController parentController) {
        this.parentController = parentController;
    }
}
