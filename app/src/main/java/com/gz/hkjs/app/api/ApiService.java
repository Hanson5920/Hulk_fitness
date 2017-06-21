package com.gz.hkjs.app.api;

import com.gz.hkjs.app.bean.ChooseItem;
import com.gz.hkjs.app.bean.CollectAddData;
import com.gz.hkjs.app.bean.CollectDeleteData;
import com.gz.hkjs.app.bean.FeedbackData;
import com.gz.hkjs.app.bean.FindDetail;
import com.gz.hkjs.app.bean.FindSummary;
import com.gz.hkjs.app.bean.HomesDelete;
import com.gz.hkjs.app.bean.HomesRecordData;
import com.gz.hkjs.app.bean.RecipesDetail;
import com.gz.hkjs.app.bean.RecipesSummary;
import com.gz.hkjs.app.bean.SimpleBean;
import com.gz.hkjs.app.bean.TrainVideoDetail;
import com.gz.hkjs.app.bean.UserHomeData;
import com.gz.hkjs.app.bean.Version;
import com.gz.hkjs.app.bean.VideoData;

import java.util.HashMap;

import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.POST;
import rx.Observable;

import static com.gz.hkjs.app.api.ApiConstants.API_Choose_LIST;
import static com.gz.hkjs.app.api.ApiConstants.API_FIND_DETAIL;
import static com.gz.hkjs.app.api.ApiConstants.API_FIND_LIST;
import static com.gz.hkjs.app.api.ApiConstants.API_HOME_ADD_TRAIN;
import static com.gz.hkjs.app.api.ApiConstants.API_Homes_Delete;
import static com.gz.hkjs.app.api.ApiConstants.API_RECIPES_DETAIL;
import static com.gz.hkjs.app.api.ApiConstants.API_RECIPES_LIST;
import static com.gz.hkjs.app.api.ApiConstants.API_RECIPES_TAB;
import static com.gz.hkjs.app.api.ApiConstants.API_TRAIN_VEDIO_DETAIL_LIST;
import static com.gz.hkjs.app.api.ApiConstants.API_USER_ADVICE_CREATE;
import static com.gz.hkjs.app.api.ApiConstants.API_USER_COLLECTION_ADD;
import static com.gz.hkjs.app.api.ApiConstants.API_USER_COLLECTION_DELETE;
import static com.gz.hkjs.app.api.ApiConstants.API_USER_COLLECTION_Find_LIST;
import static com.gz.hkjs.app.api.ApiConstants.API_USER_COLLECTION_RECIPE_LIST;
import static com.gz.hkjs.app.api.ApiConstants.API_USER_HOME_ADDRECORD;
import static com.gz.hkjs.app.api.ApiConstants.API_USER_HOME_DATA;
import static com.gz.hkjs.app.api.ApiConstants.API_USER_HOME_RECORD;
import static com.gz.hkjs.app.api.ApiConstants.API_VEDIO_LIST;
import static com.gz.hkjs.app.api.ApiConstants.API_VERSION;

/**
 * des:ApiService
 * Created by zzy
 * on 2017年3月31日
 */
public interface ApiService {

    @FormUrlEncoded
    @POST(API_FIND_DETAIL)
    Observable<FindDetail> getNewDetail(
            @Header("Cache-Control") String cacheControl,
            @FieldMap HashMap<String, String> map);

    @FormUrlEncoded
    @POST(API_RECIPES_DETAIL)
    Observable<RecipesDetail> getRecipesDetail(
            @Header("Cache-Control") String cacheControl,
            @FieldMap HashMap<String, String> map);

    @FormUrlEncoded
    @POST(API_FIND_LIST)
    Observable<FindSummary> getFindsList(
            @Header("Cache-Control") String cacheControl,
            @FieldMap HashMap<String, String> map);

    @FormUrlEncoded
    @POST(API_RECIPES_LIST)
    Observable<RecipesSummary> getRecipesList(
            @Header("Cache-Control") String cacheControl,
            @FieldMap HashMap<String, String> map);

    @FormUrlEncoded
    @POST(API_RECIPES_TAB)
    Observable<RecipesSummary> getRecipesTab(
            @Header("Cache-Control") String cacheControl,
            @FieldMap HashMap<String, String> map);

    @FormUrlEncoded
    @POST(API_VERSION)
    Observable<Version> getVersionData(
            @Header("Cache-Control") String cacheControl,
            @FieldMap HashMap<String, String> map);

    @FormUrlEncoded
    @POST(API_VEDIO_LIST)
    Observable<VideoData> getVideoList(
            @Header("Cache-Control") String cacheControl,
            @FieldMap HashMap<String, String> map);

    @FormUrlEncoded
    @POST(API_Choose_LIST)
    Observable<ChooseItem> getChooseItemList(
            @Header("Cache-Control") String cacheControl,
            @FieldMap HashMap<String, String> map);

    @FormUrlEncoded
    @POST(API_TRAIN_VEDIO_DETAIL_LIST)
    Observable<TrainVideoDetail> getTrainVedioDetail(
            @Header("Cache-Control") String cacheControl,
            @FieldMap HashMap<String, String> map);

    @FormUrlEncoded
    @POST(API_USER_HOME_DATA)
    Observable<UserHomeData> getHomeDataList(
            @Header("Cache-Control") String cacheControl,
            @FieldMap HashMap<String, String> map);

    @FormUrlEncoded
    @POST(API_Homes_Delete)
    Observable<HomesDelete> deleteHomesRequest(
            @Header("Cache-Control") String cacheControl,
            @FieldMap HashMap<String, String> map);

    @FormUrlEncoded
    @POST(API_HOME_ADD_TRAIN)
    Observable<SimpleBean> addTrainHomesRequest(
            @Header("Cache-Control") String cacheControl,
            @FieldMap HashMap<String, String> map);


    @FormUrlEncoded
    @POST(API_USER_ADVICE_CREATE)
    Observable<FeedbackData> getAdviceCreate(
            @Header("Cache-Control") String cacheControl,
            @FieldMap HashMap<String, String> map);

    @FormUrlEncoded
    @POST(API_USER_COLLECTION_RECIPE_LIST)
    Observable<RecipesSummary> getCollectionRecipeList(
            @Header("Cache-Control") String cacheControl,
            @FieldMap HashMap<String, String> map);

    @FormUrlEncoded
    @POST(API_USER_COLLECTION_Find_LIST)
    Observable<FindSummary> getCollectionFindList(
            @Header("Cache-Control") String cacheControl,
            @FieldMap HashMap<String, String> map);

    @FormUrlEncoded
    @POST(API_USER_COLLECTION_ADD)
    Observable<CollectAddData> getCollectionAdd(
            @Header("Cache-Control") String cacheControl,
            @FieldMap HashMap<String, String> map);

    @FormUrlEncoded
    @POST(API_USER_COLLECTION_DELETE)
    Observable<CollectDeleteData> getCollectionDelete(
            @Header("Cache-Control") String cacheControl,
            @FieldMap HashMap<String, String> map);


    @FormUrlEncoded
    @POST(API_USER_HOME_RECORD)
    Observable<HomesRecordData> getHomesRecordList(
            @Header("Cache-Control") String cacheControl,
            @FieldMap HashMap<String, String> map);

    @FormUrlEncoded
    @POST(API_USER_HOME_ADDRECORD)
    Observable<SimpleBean> TrainAddRecord(
            @Header("Cache-Control") String cacheControl,
            @FieldMap HashMap<String, String> map);

}
