# Change Log

## [1.0.5] - 2019-03-14
### Added
- Open url and / or  placement by HSN
- Notification render constraints

### Fixed
- Checking is the app in the foreground for android v9
- Showing HSN without the network connection
- Internal bugs

## [1.0.4] - 2019-01-31
### Added
- `TBHomeScreenNewsManager.init()` method without context
- Notifications KillSwitch

### Changed
- `DISABLE_KEYGUARD` permission is now optional

### Deprecated
-  `TBHomeScreenNewsManager.init()` method with context

## [1.0.3] - 2019-01-23
### Fixed
- Internal bugs
- Showing Home Screen News when app already on top

### Added
- Config version verification
- Screen unlock by notification click

### Changed
- Remove redundant image prefetching
- Rename the First Screen Delivery to Home Screen News

## [0.11.4] - 2018-12-20
### Added
- Unlock screen by attribution click
- Time windows checking for the first screen showing
- More analytics events
- Min time interval for showing the first screen
- Last used Sim country cache

### Fixed
- Notification layouts
- Analytics frequency

## [0.11.3] - 2018-12-15
### Fixed
- Network crash

## [0.11.2] - 2018-12-04
### Fixed
- Internal bugs
- Hide next button for notification collapsed layout when there is no next page

### Added
- Customization for single default collapsed layout
- New notification collapsed layout 

## [0.11.1] - 2018-11-22
### Fixed
- don't use external resources for color

## [0.11.0] - 2018-11-20
### Fixed
- Analytics events
- Notification layouts
- Single item notification actions
- Notification layouts 
- Infinite loop when failed to load next image 
- Continue from the same notification page after item was deleted
 
### Added
- Multiple notifications layout (collapsed & expanded)
- New collapsed notification layout
- Ability to have a list of placements in the analytics
- Notification Attribution Viewer
- Sponsored localization
- Branding view for sponsored items

## [0.10.3] - 2018-10-17
### Fixed
- Analytics events

## [0.10.2] - 2018-09-21
### Fixed
- Internal bugs

## [0.10.1] - 2018-09-05
### Added
- More analytics events

### Changed
- Getting sim country from TaboolaApi

## [0.10.0] - 2018-09-04
### Added
- Read more tip (item that leads to the app without opening a story)
- Support for config A/B testing
- Sending analytics to TRC

### Changed
- Don't fetch notification content if notifications are blocked by user
- SDK+ config is updated on each notification refresh job run
- Using config v1.2

### Fixed
- Crash on application update
- Other minor bugs

## [0.9.5] - 2018-07-31
### Fixed
- Memory leak related to content auto-switch

## [0.9.4] - 2018-07-25
### Changed
- Dismiss previous notification even if no items were returned for next notification refresh

### Fixed
- Notification refreshing before refresh interval have passed
- Prevent auto switching content after it was already deleted
- Send render event for items with hint

## [0.9.3] - 2018-07-24
### Added
- send analytics for a configurable fraction of users

### Changed
- Send "available" and "visible" events on notification dismiss
- Use new timestamp pattern for analytics

## [0.9.2] - 2018-07-24
### Added
- ResponseItemCount property to contentRefresh event

### Changed
- Treat empty content as a successful response
- Use a dynamic dependency version for TaboolaApi

### Fixed
- Requests to Kibana

## [0.9.1] - 2018-07-23
### Added
- EmptyContentFromRemote analytics event

### Changed
- Use Taboola SDK 2.0.22

### Fixed
- Hide arrows if only one item was fetched (not just after deletion)

## [0.9.0] - 2018-07-16
### Added
- Config expiration time

### Changed
- Notification item is now removed after click
- Notification item is now removed if thumbnail failed to load

### Removed
- Methods deprecated in v0.8.4

### Fixed
- Internal bug fixes

## [0.8.5] - 2018-06-26
### Changed
- `TBNotificationManager#enable()` call will be ignored if notification refresh job is already running
- Notification hint replaced with animation
- Notification content is no longer cached

### Fixed
- Internal bug fixes

## [0.8.4] - 2018-06-14
### Added
- Application name in notificaiton
- `TaboolaPlus.init()` methods that don't require context
- Transitive dependencies are now included in pom file

### Deprecated
- `TaboolaPlus.init()` methods with context
- `TaboolaPlus.getInitializedInstance()` methods

## [0.8.3] - 2018-05-25
### Added
- Notification refresh job now survives app update

### Fixed
- Internal bug fixes
