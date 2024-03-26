package ru.dayone.lifestylehub.utils.status

import ru.dayone.lifestylehub.data.local.activity.ActivityEntity
import ru.dayone.lifestylehub.utils.FailureCode

open class ActivityStatus {
    open class Remote{
        data class Succeed(val activity: ActivityEntity): Remote()
        data class Failed(val code: FailureCode) : Remote()
    }

    open class Local{
        data class Succeed(val activities: List<ActivityEntity>): Local()
        class Failed : Local()
    }
}