

#include "Skybox.h"
#include "main.h"
#include "game/game.h"
#include "vendor/armhook/patch.h"
#include "net/netgame.h"
#include "game/Models/ModelInfo.h"

extern CGame* pGame;
extern CNetGame* pNetGame;

CObject* Skybox::m_pSkyBox = nullptr;
RwTexture* pSkyTexture = nullptr;
bool Skybox::m_bNeedRender = true;
float Skybox::m_fRotSpeed = 0.005f;
float Skybox::m_fRotate = 0.0f;

void Skybox::Init() {
    FLog("2");
    if (!pGame || !pGame->FindPlayerPed() || !pGame->FindPlayerPed()->m_pPed) return;
    FLog("3");
    if (!CModelInfo::ms_modelInfoPtrs[18659]) return;
    FLog("4");

    CVector pos = pGame->FindPlayerPed()->m_pPed->m_matrix->m_pos;
    CVector zero = { 0.0f, 0.0f, 0.0f };
    FLog("5");

    m_pSkyBox = pGame->NewObject(18659, {pos.x, pos.y, pos.z - 10.0f}, zero, 1.0f);
    FLog("6");
}

void Skybox::Process() {
    FLog("1");
    if (!m_pSkyBox) Init();
    FLog("7");
    if (!m_pSkyBox) return;
    FLog("8");
    if (!pGame || !pGame->FindPlayerPed() || !pGame->FindPlayerPed()->m_pPed) return;
    FLog("9");
    m_pSkyBox->m_pEntity->m_matrix;

    CVector pos = pGame->FindPlayerPed()->m_pPed->m_matrix->m_pos;

    m_fRotate += m_fRotSpeed;

    m_pSkyBox->MoveTo(pos.x, pos.y, pos.z, 1.0f, 0.0f, 0.0f, m_fRotate);
    FLog("10");
    m_pSkyBox->m_pEntity->UpdateRW();
    FLog("11");
    m_pSkyBox->m_pEntity->UpdateRwFrame();
    FLog("12");
    ReTexture();
}

void Skybox::ReTexture() {
    FLog("13");
    if (!m_pSkyBox || !m_pSkyBox->m_pEntity || !m_pSkyBox->m_pEntity->m_pRwAtomic) return;
    FLog("14");
    RpAtomic* pAtomic = m_pSkyBox->m_pEntity->m_pRwAtomic;
    FLog("15");
    if (!pAtomic || !*(uintptr_t*)((uintptr_t)pAtomic + 4)) return;
    FLog("16");
    RwFrame* pFrame = *(RwFrame**)((uintptr_t)pAtomic + 8);
    FLog("17");
    if (!pFrame) return;
    FLog("18");
    int iHours = pNetGame->m_pNetSet->byteWorldTime_Hour;
    FLog("19");
    if (iHours >= 6 && iHours <= 11) SetTexture("BR_Nebo_Rasv");
    else if (iHours >= 12 && iHours <= 17) SetTexture("BR_Nebo_Day");
    else if (iHours >= 18 && iHours <= 22) SetTexture("BR_Nebo_Zakat");
    else SetTexture("BR_Nebo_Nght");
    FLog("26");
    RwFrameForAllObjects(
            pFrame,
            reinterpret_cast<RwObjectCallBack>(Skybox::RwFrameForAllObjectsCallback),
            m_pSkyBox
    );
    FLog("32");
}

uintptr_t Skybox::RwFrameForAllObjectsCallback(uintptr_t object, CObject* pObject) {
    FLog("27");
    RpAtomic* pAtomic = reinterpret_cast<RpAtomic*>(object);
    FLog("28");
    if (!pAtomic) return 0;
    FLog("29");
    RpGeometry* pGeom = RpAtomicGetGeometry(pAtomic);
    if (!pGeom) return 0;
    FLog("30");
    for (int i = 0; i < pGeom->matList.numMaterials; ++i) {
        RpMaterial* mat = pGeom->matList.materials[i];
        if (mat && pSkyTexture) {
            CHook::CallFunction<int>("_Z20RpMaterialSetTextureP10RpMaterialP9RwTexture", mat, pSkyTexture);
        }
    }
    FLog("31");

    return 1;
}

void Skybox::SetTexture(const char* texName) {
    FLog("20");
    if (!texName || !*texName) return;
    FLog("21");
    RwTexture* newTex = CUtil::LoadTextureFromDB("samp", texName);
    FLog("22");
    if (!newTex) {
        FLog("SetTexture - failed to load texture %s", texName);
        return;
    }
    FLog("23");
    if (pSkyTexture) {
        FLog("24");
        (*RwTextureDestroy)(pSkyTexture);
    }
    FLog("25");
    pSkyTexture = newTex;
}

bool Skybox::IsNeedRender() {
    return m_bNeedRender;
}

CObject* Skybox::GetSkyObject() {
    return m_pSkyBox;
}