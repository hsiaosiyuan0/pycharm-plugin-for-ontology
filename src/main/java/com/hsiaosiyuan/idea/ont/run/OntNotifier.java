package com.hsiaosiyuan.idea.ont.run;

import com.intellij.notification.*;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.text.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.PrintWriter;
import java.io.StringWriter;

public class OntNotifier {
  public static final NotificationGroup NOTIFICATION_GROUP_ID = NotificationGroup.toolWindowGroup(
      "Ontology Messages", OntConsoleToolWindowFactory.ID);
  public static final NotificationGroup IMPORTANT_ERROR_NOTIFICATION = new NotificationGroup(
      "Ontology Important Messages", NotificationDisplayType.STICKY_BALLOON, true);
  public static final NotificationGroup STANDARD_NOTIFICATION = new NotificationGroup(
      "Ontology Notifications", NotificationDisplayType.BALLOON, true);
  public static final NotificationGroup SILENT_NOTIFICATION = new NotificationGroup(
      "Ontology Silent Notifications", NotificationDisplayType.NONE, true);

  private final @NotNull
  Project myProject;


  public static OntNotifier getInstance(@NotNull Project project) {
    return ServiceManager.getService(project, OntNotifier.class);
  }

  public OntNotifier(@NotNull Project project) {
    myProject = project;
  }

  @NotNull
  public static Notification createNotification(@NotNull NotificationGroup notificationGroup,
                                                @NotNull String title,
                                                @NotNull String message,
                                                @NotNull NotificationType type,
                                                @Nullable NotificationListener listener) {
    // title can be empty; message can't be neither null, nor empty
    if (StringUtil.isEmptyOrSpaces(message)) {
      message = title;
      title = "";
    }
    // if both title and message were empty, then it is a problem in the calling code => Notifications engine assertion will notify.
    return notificationGroup.createNotification(title, message, type, listener);
  }

  @NotNull
  public Notification notify(@NotNull NotificationGroup notificationGroup,
                             @NotNull String title,
                             @NotNull String message,
                             @NotNull NotificationType type,
                             @Nullable NotificationListener listener) {
    Notification notification = createNotification(notificationGroup, title, message, type, listener);
    return notify(notification);
  }

  @NotNull
  public Notification notify(@NotNull NotificationGroup notificationGroup,
                             @NotNull String title,
                             @NotNull String message,
                             @NotNull NotificationType type,
                             NotificationAction... actions) {
    Notification notification = createNotification(notificationGroup, title, message, type, null);
    for (NotificationAction action : actions) {
      notification.addAction(action);
    }
    return notify(notification);
  }

  @NotNull
  public Notification notify(@NotNull Notification notification) {
    notification.notify(myProject);
    return notification;
  }

  @NotNull
  public Notification notifyError(@NotNull String title, @NotNull String message) {
    return notifyError(title, message, (NotificationListener) null);
  }

  @NotNull
  public Notification notifyError(@NotNull String title, @NotNull Exception e) {
    String msg = e.getMessage();
    if (msg == null) {
      StringWriter sw = new StringWriter();
      PrintWriter pw = new PrintWriter(sw);
      e.printStackTrace(pw);
      msg = sw.toString();
    }
    return notifyError(title, msg, (NotificationListener) null);
  }

  @NotNull
  public Notification notifyError(@NotNull String title, @NotNull String message, @Nullable NotificationListener listener) {
    return notify(IMPORTANT_ERROR_NOTIFICATION, title, message, NotificationType.ERROR, listener);
  }

  @NotNull
  public Notification notifyError(@NotNull String title, @NotNull String message, NotificationAction... actions) {
    return notify(IMPORTANT_ERROR_NOTIFICATION, title, message, NotificationType.ERROR, actions);
  }

  @NotNull
  public Notification notifyWeakError(@NotNull String message) {
    return notify(NOTIFICATION_GROUP_ID, "", message, NotificationType.ERROR);
  }

  @NotNull
  public Notification notifySuccess(@NotNull String message) {
    return notifySuccess("", message);
  }

  @NotNull
  public Notification notifySuccess(@NotNull String title, @NotNull String message) {
    return notifySuccess(title, message, null);
  }

  @NotNull
  public Notification notifySuccess(@NotNull String title, @NotNull String message, @Nullable NotificationListener listener) {
    return notify(NOTIFICATION_GROUP_ID, title, message, NotificationType.INFORMATION, listener);
  }

  @NotNull
  public Notification notifyImportantInfo(@NotNull String title, @NotNull String message) {
    return notify(IMPORTANT_ERROR_NOTIFICATION, title, message, NotificationType.INFORMATION);
  }

  @NotNull
  public Notification notifyImportantInfo(@NotNull String title, @NotNull String message, @Nullable NotificationListener listener) {
    return notify(IMPORTANT_ERROR_NOTIFICATION, title, message, NotificationType.INFORMATION, listener);
  }

  @NotNull
  public Notification notifyInfo(@NotNull String message) {
    return notifyInfo("", message);
  }

  @NotNull
  public Notification notifyInfo(@NotNull String title, @NotNull String message) {
    return notifyInfo(title, message, null);
  }

  @NotNull
  public Notification notifyInfo(@NotNull String title, @NotNull String message, @Nullable NotificationListener listener) {
    return notify(NOTIFICATION_GROUP_ID, title, message, NotificationType.INFORMATION, listener);
  }

  @NotNull
  public Notification notifyMinorWarning(@NotNull String title, @NotNull String message) {
    return notifyMinorWarning(title, message, null);
  }

  @NotNull
  public Notification notifyMinorWarning(@NotNull String title, @NotNull String message, @Nullable NotificationListener listener) {
    return notify(STANDARD_NOTIFICATION, title, message, NotificationType.WARNING, listener);
  }

  @NotNull
  public Notification notifyWarning(@NotNull String title, @NotNull String message) {
    return notifyWarning(title, message, null);
  }

  @NotNull
  public Notification notifyWarning(@NotNull String title, @NotNull String message, @Nullable NotificationListener listener) {
    return notify(NOTIFICATION_GROUP_ID, title, message, NotificationType.WARNING, listener);
  }

  @NotNull
  public Notification notifyImportantWarning(@NotNull String title, @NotNull String message) {
    return notify(IMPORTANT_ERROR_NOTIFICATION, title, message, NotificationType.WARNING);
  }

  @NotNull
  public Notification notifyImportantWarning(@NotNull String title, @NotNull String message, @Nullable NotificationListener listener) {
    return notify(IMPORTANT_ERROR_NOTIFICATION, title, message, NotificationType.WARNING, listener);
  }

  @NotNull
  public Notification notifyMinorInfo(@NotNull String title, @NotNull String message) {
    return notifyMinorInfo(title, message, (NotificationListener) null);
  }

  @NotNull
  public Notification notifyMinorInfo(@NotNull String title, @NotNull String message, @Nullable NotificationListener listener) {
    return notify(STANDARD_NOTIFICATION, title, message, NotificationType.INFORMATION, listener);
  }

  @NotNull
  public Notification notifyMinorInfo(@NotNull String title, @NotNull String message, NotificationAction... actions) {
    return notify(STANDARD_NOTIFICATION, title, message, NotificationType.INFORMATION, actions);
  }

  public Notification logInfo(@NotNull String title, @NotNull String message) {
    return notify(SILENT_NOTIFICATION, title, message, NotificationType.INFORMATION);
  }

  public void showNotificationAndHideExisting(@NotNull Notification notificationToShow, @NotNull Class<? extends Notification> klass) {
    hideAllNotificationsByType(klass);
    notificationToShow.notify(myProject);
  }

  public void hideAllNotificationsByType(@NotNull Class<? extends Notification> klass) {
    NotificationsManager notificationsManager = NotificationsManager.getNotificationsManager();
    for (Notification notification : notificationsManager.getNotificationsOfType(klass, myProject)) {
      notification.expire();
    }
  }
}
