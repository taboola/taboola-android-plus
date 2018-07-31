# Change Log


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
