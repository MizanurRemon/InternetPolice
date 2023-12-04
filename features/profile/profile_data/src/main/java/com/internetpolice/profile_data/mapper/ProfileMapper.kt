package com.internetpolice.profile_data.mapper

import com.internetpolice.core.common.util.getFormattedDate
import com.internetpolice.core.network.dto.CommonResponseDto
import com.internetpolice.core.network.dto.CompleteProfileDto
import com.internetpolice.core.network.dto.UserDataUpdateDto
import com.internetpolice.core.network.dto.UserNameVerifyDto
import com.internetpolice.core.network.dto.UserPoint
import com.internetpolice.core.network.dto.UserPointsDto
import com.internetpolice.core.network.dto.Vote
import com.internetpolice.core.network.dto.VoteListDto
import com.internetpolice.profile_domain.model.CommonModel
import com.internetpolice.profile_domain.model.CompleteProfileModel
import com.internetpolice.profile_domain.model.UserDataUpdateModel
import com.internetpolice.profile_domain.model.UserNameVerifyModel
import com.internetpolice.profile_domain.model.UserPointModel
import com.internetpolice.profile_domain.model.VoteModel


fun UserNameVerifyDto.toUserNameVerifyModel(): UserNameVerifyModel {
    return UserNameVerifyModel(
        id = this.id, userId = this.userId, nickname = this.nickname

    )
}

fun UserDataUpdateDto.toUserDataUpdateModel(): UserDataUpdateModel {
    return UserDataUpdateModel(
        id = this.id, userId = this.userId, nickname = this.nickname, ageCategory = ageCategory

    )
}

fun CommonResponseDto.toCommonModel(): CommonModel {
    return CommonModel(
        result = this.result

    )
}

fun CompleteProfileDto.toCompleteProfileModel(): CompleteProfileModel {
    return CompleteProfileModel(
        id = this.id,
        userId = this.userId,
        nickname = this.nickname,
        isProfileCompleted = this.isProfileCompleted,
        avatarImagePath = this.avatarImagePath,
        rank = this.rank
    )
}

fun UserPoint.toUserPointModel(): UserPointModel {
    return UserPointModel(
        point = point,
        rewardCode = rewardCode

    )
}

fun UserPointsDto.toUserPointModels(): List<UserPointModel> {
    return this.userPoints?.let {
        it.userPointList?.map { userPoint ->
            userPoint.toUserPointModel()

        }
    } ?: emptyList()
}

fun Vote.toVoteModel(): VoteModel {
    return VoteModel(
        category = category,
        createdDate = getFormattedDate(createdDate, "dd/MM/yyyy"),
        description = description,
        domainName = domainName,
        id = id,
        voteStatus = voteStatus,
        voteType = voteType
    )
}

fun VoteListDto.toVoteModels(): List<VoteModel> {

    return this.voteList.map { userPoint ->
        userPoint.toVoteModel()

    }
}





