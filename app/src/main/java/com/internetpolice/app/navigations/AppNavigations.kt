package com.internetpolice.app.navigations

import AvatarScreen
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.gson.Gson
import com.internetpolice.app.BuildConfig
import com.internetpolice.app.SplashScreen
import com.internetpolice.app.home.GuestPreAccountCreationInfoScreen
import com.internetpolice.app.home.HomeScreen
import com.internetpolice.app.subscriptions.MySubscriptionDetailsScreen
import com.internetpolice.app.subscriptions.SubscriptionCancelScreen
import com.internetpolice.app.waring.WarningScreen
import com.internetpolice.app.waring.toDomainModel
import com.internetpolice.auth_presentation.forgot_pass.ForgetPassEmailInput
import com.internetpolice.auth_presentation.forgot_pass.ForgetPasswordCheckYourMailScreen
import com.internetpolice.auth_presentation.login.SignInScreen
import com.internetpolice.auth_presentation.registration.SignUpScreen
import com.internetpolice.auth_presentation.screens.AlwaysProtectedScreen
import com.internetpolice.auth_presentation.screens.AuthChooseScreen
import com.internetpolice.auth_presentation.screens.PasswordUpdatedScreen
import com.internetpolice.auth_presentation.screens.SignUpNotAvailableScreen
import com.internetpolice.auth_presentation.screens.SignUpNotAvailableSuccessScreen
import com.internetpolice.auth_presentation.verify.VerifiedEmailDoneScreen
import com.internetpolice.auth_presentation.verify.VerifyEmailOTPScreen
import com.internetpolice.core.common.navigation.Route
import com.internetpolice.core.network.dto.DomainDetailsDto
import com.internetpolice.news_presentation.News.NewsTabScreen
import com.internetpolice.news_presentation.NewsDetails.NewsDetailsScreen
import com.internetpolice.notification_presentation.NotificationScreen
import com.internetpolice.onboardings_presentation.InitOnboardingScreen
import com.internetpolice.onboardings_presentation.OnBoardingPersonaliseProfileScreen
import com.internetpolice.onboardings_presentation.VideoOnBoardingScreen
import com.internetpolice.profile_domain.model.VoteModel
import com.internetpolice.profile_presentation.email_update.EmailUpdatedScreen
import com.internetpolice.profile_presentation.email_update.SetNewEmailScreen
import com.internetpolice.profile_presentation.password_change.PasswordChangeScreen
import com.internetpolice.profile_presentation.acc_settings.ManageAccountScreen
import com.internetpolice.profile_presentation.avater.AvatarNameInputScreen
import com.internetpolice.profile_presentation.info.RankingEarningXPScreen
import com.internetpolice.profile_presentation.progress.ProfileScreen
import com.internetpolice.profile_presentation.progress.ReportDetailsScreen
import com.internetpolice.profile_presentation.progress.VoteModelArgType
import com.internetpolice.report_domain.model.DomainModel
import com.internetpolice.report_presentation.ReportViewModel
import com.internetpolice.report_presentation.report.ReportDescriptionInputScreen
import com.internetpolice.report_presentation.report.ReportIntroScreen
import com.internetpolice.report_presentation.report.ReportReceivedDoneScreen
import com.internetpolice.report_presentation.report.ReportScreen
import com.internetpolice.report_presentation.screen_website.ReportWebsiteScreen
import com.internetpolice.report_presentation.screen_website.ScreenWebsiteScreen
import com.internetpolice.report_presentation.screen_website.WebSearchScreen
import com.internetpolice.report_presentation.screen_website.WebsiteSearchDetailsScreen
import com.internetpolice.report_presentation.utils.DomainModelArgType
import com.internetpolice.settings_presentation.screens.ChangeLogDetailsScreen
import com.internetpolice.settings_presentation.screens.ChangeLogScreen
import com.internetpolice.settings_presentation.screens.ConditionScreen
import com.internetpolice.settings_presentation.screens.HelpContactScreen
import com.internetpolice.settings_presentation.screens.language.LanguageScreen
import com.internetpolice.settings_presentation.screens.MailSuccessScreen
import com.internetpolice.settings_presentation.screens.ReportSuccessScreen
import com.internetpolice.settings_presentation.screens.about_us.AboutUsScreen

import com.internetpolice.settings_presentation.screens.faq.FAQScreen
import com.internetpolice.settings_presentation.screens.getTestChangeLogDetailsListResponse
import com.internetpolice.settings_presentation.screens.report_problem.ReportProblemScreen
import com.internetpolice.settings_presentation.screens.settings.SettingsScreen
import com.internetpolice.core.common.R as CommonR


@OptIn(ExperimentalMaterial3Api::class, ExperimentalPagerApi::class)
@Composable
fun IPApp(
    domainDetailsDto: DomainDetailsDto? = null,
    domainModel: DomainModel? = null,
    navController: NavHostController = rememberNavController(),
) {
    val context = LocalContext.current
    val snackBarHostState = remember { SnackbarHostState() }
    var isChangingEmail = remember { false }

    Scaffold(snackbarHost = { SnackbarHost(snackBarHostState) }) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = if (domainDetailsDto != null) Route.WARNING else Route.SPLASH,
            modifier = Modifier.padding(innerPadding)
        ) {

            composable(Route.SPLASH) {
                SplashScreen(navController, domainModel = domainModel)
            }
            composable(Route.WARNING) {
                if (domainDetailsDto != null) {

                    val model = domainDetailsDto.toDomainModel()
                    WarningScreen(
                        domainDetailsDto = domainDetailsDto,
                        onBack = { navController.navigate(Route.HOME) },
                        onGoBackSafely = { navController.navigate(Route.HOME) },
                        websiteDetails = {
                            navController.navigate(Route.WEB_SEARCH_DETAILS + "/${model}")
                        }
                    )
                }
            }
            composable(route = Route.LANGUAGE_INIT) {
                LanguageScreen(navController, isInitial = true)
            }
            composable(route = Route.ONBOARDING) {
                InitOnboardingScreen(navController)
            }
            composable(
                route = Route.VIDEO_ONBOARDING + "/{isFaq}",
                arguments = listOf(navArgument("isFaq") {
                    type = NavType.BoolType
                })
            ) {
                VideoOnBoardingScreen(
                    navController,
                    isFaq = it.arguments?.getBoolean("isFaq") ?: false,
                    onButtonClick = {
                        val isFaq = it.arguments?.getBoolean("isFaq") ?: false
                        if (isFaq) {
                            navController.popBackStack()
                        } else {
                            navController.navigate(Route.PERSONALISE_PROFILE_ONBOARDING)
                        }
                    }
                )

            }
            composable(route = Route.PERSONALISE_PROFILE_ONBOARDING) {
                OnBoardingPersonaliseProfileScreen(onGoAvatar = {
                    navController.navigate(Route.AVATAR_NAME_INPUT)
                }, onGoHome = {
                    navController.navigate(Route.HOME)
                })
            }
            composable(route = Route.AVATAR_NAME_INPUT) {
                AvatarNameInputScreen(snackBarHostState, onBack = {
                    navController.popBackStack(Route.HOME, inclusive = false)
                }, onGoToAvatarCreation = {
                    navController.navigate(Route.AVATAR_CREATION + "/${it}")
                })
            }
            composable(
                route = Route.AVATAR_CREATION + "/{name}", arguments = listOf(navArgument("name") {
                    type = NavType.StringType
                })
            ) { navBackStackEntry ->
                val name = navBackStackEntry.arguments?.getString("name") ?: ""
                AvatarScreen(snackBarHostState, name = name, onBackClick = {
                    navController.navigateUp()
                }, onGoToProfile = {
                    navController.navigate(Route.PROFILE + "/false")
                })
            }
            composable(route = Route.AUTH_CHOOSE) {
                AuthChooseScreen(onGuestClick = {
                    navController.navigate(Route.GUEST_PRE_ACC_CREATION_INFO)
                }, onSignIn = {
                    navController.navigate(Route.SIGN_IN)
                }, onSignUp = {
                    navController.navigate(Route.SIGN_UP)
                }, navigateSignUpNotAvailable = {
                    navController.navigate(Route.SIGN_UP_NOT_AVAILABLE)
                }
                )
            }
            composable(
                route = Route.NOTIFICATION_ON_OFF + "/{isHome}",
                arguments = listOf(navArgument("isHome") {
                    type = NavType.BoolType
                })
            ) {
                NotificationScreen(
                    navController, isHome = it.arguments?.getBoolean("isHome") ?: false
                )
            }
            composable(route = Route.HOME) {
                HomeScreen(navController, snackBarHostState, onReportClick = {
                    navController.navigate(Route.REPORT_INTRO_WEBSITE)
                }, onScreenWebsiteClick = {
                    navController.navigate(Route.SCREEN_WEBSITE)
                }, onWebSearchResultClick = { model ->
                    navController.navigate(Route.WEB_SEARCH_DETAILS + "/${model}")
                }, onMySubscriptionClick = {
                    navController.navigate(Route.MY_SUBSCRIPTION)
                }, onNewsItemClick = { id ->
                    navController.navigate(Route.NEWS_DETAILS + "/$id")
                }, onSeeAllNewsClick = {
                    navController.navigate(Route.NEWS_TAB)
                }, onMyProfileClick = { isGuest ->
                    navController.navigate(Route.PROFILE + "/$isGuest")
                }, onAccountSettingsClick = {
                    navController.navigate(Route.MANAGE_ACCOUNT)
                }, onSettingsClick = {
                    navController.navigate(Route.SETTINGS)
                }, onAccountCreateClick = {
                    navController.navigate(Route.SIGN_UP)
                }, onProfileEditClick = {
                    navController.navigate(Route.AVATAR_NAME_INPUT)
                })
            }
            composable(route = Route.SETTINGS) {
                SettingsScreen(versionName = BuildConfig.VERSION_NAME, onBack = {
                    navController.popBackStack(Route.HOME, inclusive = false)
                }, onLanguageClick = {
                    navController.navigate(Route.LANGUAGE)
                }, onFAQClick = {
                    navController.navigate(Route.FAQ)
                }, onAboutUsClick = {
                    navController.navigate(Route.ABOUT_US)
                }, onHelpContactClick = {
                    navController.navigate(Route.HELP_CONTACT)
                }, onReportProblemClick = {
                    navController.navigate(Route.REPORT_PROBLEM)
                }, onConditionClick = {
                    navController.navigate(Route.CONDITION)
                }, onChangeLogClick = {
                    navController.navigate(Route.CHANGE_LOG)
                })
            }
            composable(route = Route.ABOUT_US) {
                AboutUsScreen(onBack = {
                    navController.navigateUp()
                })
            }
            composable(route = Route.CONDITION) {
                ConditionScreen {
                    navController.navigateUp()
                }
            }
            composable(route = Route.CHANGE_LOG_DETAILS) {
                ChangeLogDetailsScreen(
                    versionName = BuildConfig.VERSION_NAME,
                    changeLogDetailsListResponse = getTestChangeLogDetailsListResponse()
                ) {
                    navController.navigateUp()
                }
            }
            composable(route = Route.CHANGE_LOG) {
                ChangeLogScreen(versionName = BuildConfig.VERSION_NAME, onBack = {
                    navController.navigateUp()
                }) {
                    navController.navigate(Route.CHANGE_LOG_DETAILS)
                }
            }
            composable(route = Route.HELP_CONTACT) {
                HelpContactScreen(onBack = {
                    navController.navigateUp()
                }, onSuccess = {
                    navController.navigate(Route.MAIL_SUCCESS)
                }, onFaqClick = {
                    navController.navigate(Route.FAQ)
                }, onTechnicalIssueFormClick = {
                    navController.navigate(Route.REPORT_PROBLEM)
                }, snackbarHostState = snackBarHostState

                )
            }
            composable(route = Route.REPORT_PROBLEM) {
                ReportProblemScreen(onBack = {
                    navController.navigateUp()
                }, navController,
                    snackBarHostState,
                    onGeneralClick = {
                        navController.navigate(Route.HELP_CONTACT)
                    })
            }
            composable(route = Route.REPORT_SUCCESS) {
                ReportSuccessScreen(onBack = {
                    navController.popBackStack(Route.SETTINGS, inclusive = false)
                })
            }
            composable(route = Route.MAIL_SUCCESS) {
                MailSuccessScreen(onBack = {
                    navController.popBackStack(Route.SETTINGS, inclusive = false)
                })
            }
            composable(route = Route.FAQ) {
                FAQScreen(onBack = {
                    navController.navigateUp()
                }, videoOnboarding = { isFaq ->
                    navController.navigate(Route.VIDEO_ONBOARDING + "/$isFaq")
                })
            }


            composable(route = Route.LANGUAGE) {
                LanguageScreen(navController, isInitial = false)
            }

            composable(
                route = Route.PROFILE + "/{isGuest}", arguments = listOf(navArgument("isGuest") {
                    type = NavType.BoolType
                })
            ) {
                ProfileScreen(
                    navController = navController,
                    isGuest = it.arguments?.getBoolean("isGuest") ?: false,
                    onBack = {
                        navController.navigate(Route.HOME) {
                            popUpTo(navController.graph.id) {
                            }
                        }
                    },
                    onInfoClick = {
                        navController.navigate(Route.RANKING_EARNING_XP)
                    },
                    goToAvatar = { name ->
                        navController.navigate(Route.AVATAR_CREATION + "/${name}")
                    }
                )
            }
            composable(route = Route.MANAGE_ACCOUNT) {
                ManageAccountScreen(navController, snackBarHostState, onChangePassword = {
                    navController.navigate(Route.SET_NEW_PASSWORD)
                }, onChangeEmail = {
                    navController.navigate(Route.SET_NEW_EMAIL)
                }, onCustomizeAvatar = {
                    navController.navigate(Route.AVATAR_NAME_INPUT)
                }) {
                    navController.navigateUp()
                }
            }
            composable(route = Route.SET_NEW_PASSWORD) {
                PasswordChangeScreen(onBack = {
                    navController.navigateUp()
                }, snackBarHostState, navController)
            }
            composable(route = Route.SET_NEW_EMAIL) {
                SetNewEmailScreen(onBack = {
                    navController.navigateUp()
                }, onNewEmailSubmit = { email, source ->
                    isChangingEmail = true
                    navController.navigate(Route.VERIFY_OTP_EMAIL + "/$email" + "/$source")
                })
            }

            composable(
                route = Route.VERIFY_OTP_EMAIL + "/{email}/{source}",
                arguments = listOf(
                    navArgument("email") { type = NavType.StringType },
                    navArgument("source") { type = NavType.StringType }
                )
            ) {
                VerifyEmailOTPScreen(
                    navController = navController,
                    snackBarHostState,
                    email = it.arguments?.getString("email") ?: "",
                    source = it.arguments?.getString("source") ?: "",
                    onSubmit = {
                        if (isChangingEmail) {
                            isChangingEmail = false
                            navController.navigate(Route.EMAIL_UPDATED)
                        } else {
                            navController.navigate(Route.VERIFIED_EMAIL)
                        }

                    })
            }

            composable(
                route = Route.NEWS_DETAILS + "/{id}",
                arguments = listOf(navArgument("id") {
                    type = NavType.IntType
                })
            ) {
                NewsDetailsScreen(onBack = {
                    navController.navigateUp()
                })
            }
            composable(route = Route.REPORT_INTRO_WEBSITE) {
                ReportIntroScreen(onGetStarted = {
                    navController.navigate(Route.REPORT)
                }, onBackClick = {
                    navController.navigateUp()
                })
            }
            composable(route = Route.REPORT_DESCRIPTION_INPUT) {
                val parentEntry = remember(it) {
                    navController.getBackStackEntry(Route.REPORT)
                }

                val parentViewModel = hiltViewModel<ReportViewModel>(parentEntry)
                ReportDescriptionInputScreen(
                    parentViewModel, navController, snackBarHostState
                )
            }
            composable(
                route = Route.REPORT_DETAILS + "/{voteModel}",
                arguments = listOf(navArgument("voteModel") {
                    type = VoteModelArgType()
                })
            ) { navBackStackEntry ->
                val voteModel = navBackStackEntry.arguments?.getString("voteModel")
                    ?.let { Gson().fromJson(it, VoteModel::class.java) }
                ReportDetailsScreen(
                    voteModel!!, navController
                )
            }
            composable(
                route = Route.REPORT_RECEIVE_DONE + "/{model}",
                arguments = listOf(navArgument("model") {
                    type = DomainModelArgType()
                })
            ) { navBackStackEntry ->
                val domainModel = navBackStackEntry.arguments?.getString("model")
                    ?.let { Gson().fromJson(it, DomainModel::class.java) }

                ReportReceivedDoneScreen(website = domainModel?.domain ?: "", onFinished = {
                    val isScreenInBackStack = navController.backQueue.any { entry ->
                        entry.destination.route == Route.HOME
                    }
                    if (isScreenInBackStack) {
                        navController.popBackStack(Route.HOME, inclusive = false)
                    } else {
                        navController.navigate(Route.HOME)
                    }
                })
            }
            composable(route = Route.REPORT) {
                ReportScreen(onBack = {
                    navController.navigateUp()
                }, onFinished = {
                    navController.navigate(Route.REPORT_DESCRIPTION_INPUT)
                })
            }
            composable(route = Route.MY_SUBSCRIPTION) {
                MySubscriptionDetailsScreen(onBack = {
                    navController.navigateUp()
                }, onSubscriptionCancelled = {
                    navController.navigate(Route.MY_SUBSCRIPTION_CANCEL)
                })
            }
            composable(route = Route.MY_SUBSCRIPTION_CANCEL) {
                SubscriptionCancelScreen(
                    onBack = {
                        navController.navigateUp()
                    },
                )
            }
            composable(route = Route.NEWS_TAB) {
                NewsTabScreen(onBack = {
                    navController.navigateUp()
                }, onNewsItemClick = { id ->
                    navController.navigate(Route.NEWS_DETAILS + "/$id")
                })
            }

            composable(route = Route.SCREEN_WEBSITE) {
                ScreenWebsiteScreen(onBack = {
                    navController.navigateUp()
                }, onSubmit = {
                    navController.navigate(Route.WEB_SEARCH)
                })
            }
            composable(route = Route.WEB_SEARCH) {
                WebSearchScreen(onBack = {
                    navController.navigateUp()
                }) {
                    navController.navigate(Route.WEB_SEARCH_DETAILS + "/${it}")
                }
            }
            composable(
                route = Route.WEB_SEARCH_DETAILS + "/{model}",
                arguments = listOf(navArgument("model") {
                    type = DomainModelArgType()
                })
            ) { navBackStackEntry ->
                val domainModel = navBackStackEntry.arguments?.getString("model")
                    ?.let { Gson().fromJson(it, DomainModel::class.java) }

                WebsiteSearchDetailsScreen(domainModel!!, onBack = {
//                    navController.navigateUp()
                    navController.navigate(Route.HOME) {
                        popUpTo(navController.graph.id) {
                        }
                    }
                }, onNavigate = {
                    navController.navigate(Route.WEB_SEARCH_REPORT + "/${it}")

                })
            }
            composable(
                route = Route.WEB_SEARCH_REPORT + "/{model}",
                arguments = listOf(navArgument("model") {
                    type = DomainModelArgType()
                })
            ) { navBackStackEntry ->


                val domainModel = navBackStackEntry.arguments?.getString("model")
                    ?.let { Gson().fromJson(it, DomainModel::class.java) }

                ReportWebsiteScreen(domainModel!!, onBack = {
                    val isScreenInBackStack = navController.backQueue.any() { entry ->
                        entry.destination.route == Route.HOME

                    }
                    if (isScreenInBackStack) {
                        navController.popBackStack(Route.HOME, inclusive = false)

                    } else {
                        navController.navigate(Route.HOME)

                    }

                }, onFinished = {
                    navController.navigate(Route.REPORT_DESCRIPTION_INPUT)
                }, navController, snackBarHostState)
            }

            composable(route = Route.GUEST_PRE_ACC_CREATION_INFO) {
                GuestPreAccountCreationInfoScreen(onAccountCreateButtonClick = {
                    navController.navigate(Route.SIGN_UP)
                }, onFinished = {
//                    val notificationManager: NotificationManager =
//                        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//                    if (notificationManager.isNotificationPolicyAccessGranted && notificationManager.areNotificationsEnabled()) {
//                        navController.navigate(Route.HOME)
//                    } else {
                    navController.navigate(Route.HOME)
                    // }

                })
            }
            composable(route = Route.SIGN_UP) {
                SignUpScreen(
                    snackBarHostState,
                    navController,
                    redirectVerifyScreen = { email, source ->
                        navController.navigate(Route.VERIFY_OTP_EMAIL + "/$email" + "/$source")
                    }
                )
            }

            composable(
                route = Route.EMAIL_UPDATED
            ) {
                EmailUpdatedScreen(onFinished = {
                    navController.popBackStack(Route.HOME, inclusive = false)
                })
            }
            composable(route = Route.VERIFIED_EMAIL) {
                VerifiedEmailDoneScreen(navController)
            }
            composable(route = Route.SIGN_IN) {
                SignInScreen(
                    snackbarHostState = snackBarHostState,
                    navController = navController,
                    onBack = {
                        navController.navigate(Route.AUTH_CHOOSE)
                    },
                    onSignUp = {
                        navController.navigate(Route.SIGN_UP)
                    }
                )
            }
            composable(route = Route.FORGET_PASS_EMAIL_INPUT) {
                ForgetPassEmailInput(navController, snackBarHostState)
            }
            composable(
                route = Route.FORGET_PASS_CHECK_YOUR_MAIL + "/{email}",
                arguments = listOf(navArgument("email") {
                    type = NavType.StringType
                })
            ) { navBackStackEntry ->

                val email = navBackStackEntry.arguments?.getString("email") ?: ""

                ForgetPasswordCheckYourMailScreen(email) {
                    navController.navigate(Route.FORGET_PASS_NEW_PASS + "/${it}")
                }
            }
            composable(
                route = Route.FORGET_PASS_NEW_PASS + "/{email}",
                arguments = listOf(navArgument("email") {
                    type = NavType.StringType
                })
            ) { navBackStackEntry ->

                /* val email = navBackStackEntry.arguments?.getString("email") ?: ""

                ForgetPassSetNewPassScreen(email, navController, snackBarHostState) */

                navController.popBackStack(Route.SIGN_IN, inclusive = false)

            }
            composable(route = Route.FORGET_PASS_UPDATE) {
                PasswordUpdatedScreen(actionTextResId = CommonR.string.continues_as_login) {
                    navController.navigate(Route.SIGN_IN)
                }
            }
            composable(route = Route.PASS_UPDATE) {
                PasswordUpdatedScreen(actionTextResId = CommonR.string.done) {
                    navController.popBackStack(Route.HOME, inclusive = false)
                }
            }

            /*  composable(route = Route.PERMISSION) {
                  PermissionScreen(
                      navController
                  )
              }*/
            /* composable(route = Route.PERMISSION_ACCESSIBILITY) {
                 AccessibilityPermissionScreen(
                     navController,

                     )
             }*/
            /* composable(route = Route.PERMISSION_OVERLAY) {
                 OverlayPermissionScreen(
                     navController,
                 )
             }*/
            composable(route = Route.ALWAYS_PROTECTED) {
                AlwaysProtectedScreen(onContinue = {
                    navController.navigate(Route.AUTH_CHOOSE)
                })
            }

            composable(route = Route.SIGN_UP_NOT_AVAILABLE) {
                SignUpNotAvailableScreen(snackBarHostState = snackBarHostState,
                    onBack = {
                        navController.popBackStack()
                    },
                    onSuccess = {
                        navController.navigate(
                            Route.SIGN_UP_NOT_AVAILABLE_SUCCESS_SCREEN
                        ) {
//                            popUpTo(navController.graph.id) {
//                                inclusive = true
//                            }
                        }
                    }
                )
            }

            composable(route = Route.SIGN_UP_NOT_AVAILABLE_SUCCESS_SCREEN) {
                SignUpNotAvailableSuccessScreen(onDone = {
                    navController.popBackStack()
                })
            }

            composable(route = Route.RANKING_EARNING_XP) {
                RankingEarningXPScreen(onBack = {
                    navController.navigateUp()
                })
            }
        }
    }

}

fun openOverlaySettings(context: Context) {
    val intent = Intent(
        Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + context.packageName)
    )
    context.startActivity(intent)
}


fun openAccessibilitySettings(context: Context) {
    val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
    context.startActivity(intent)

}


