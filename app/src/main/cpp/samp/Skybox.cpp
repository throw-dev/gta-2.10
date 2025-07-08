

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
    if (!pGame || !pGame->FindPlayerPed() || !pGame->FindPlayerPed()->m_pPed) return;
    if (!CModelInfo::ms_modelInfoPtrs[18659]) return;

    CVector pos = pGame->FindPlayerPed()->m_pPed->m_matrix->m_pos;
    CVector zero = { 0.0f, 0.0f, 0.0f };

    m_pSkyBox = pGame->NewObject(18659, {pos.x, pos.y, pos.z - 10.0f}, zero, 1.0f);
}

void Skybox::Process() {
    if (!m_pSkyBox) Init();
    if (!m_pSkyBox) return;
    if (!pGame || !pGame->FindPlayerPed() || !pGame->FindPlayerPed()->m_pPed) return;

    m_pSkyBox->m_pEntity->m_matrix;

    CVector pos = pGame->FindPlayerPed()->m_pPed->m_matrix->m_pos;

    m_fRotate += m_fRotSpeed;

    m_pSkyBox->MoveTo(pos.x, pos.y, pos.z, 1.0f, 0.0f, 0.0f, m_fRotate);

    m_pSkyBox->m_pEntity->UpdateRW();
    m_pSkyBox->m_pEntity->UpdateRwFrame();
    RenderEntity(m_pSkyBox->m_pEntity);
    ReTexture();
}

void Skybox::ReTexture()
{
    if (!m_pSkyBox || !m_pSkyBox->m_pEntity || !m_pSkyBox->m_pEntity->m_pRwAtomic) {
        return FLog("========== Empty m_pSkyObj ============");
    } else {
        FLog("========== not Empty m_pSkyObj ============");
    }

    RpAtomic* pAtomic = m_pSkyBox->m_pEntity->m_pRwAtomic;
    if (!pAtomic || !*(uintptr_t*)((uintptr_t)pAtomic + 4)) {
        return FLog("========== Empty pAtomic ============");
    } else {
        FLog("========== not Empty pAtomic ============");
    }

    RwFrame* pFrame = *(RwFrame**)((uintptr_t)pAtomic + 8);
    if (!pFrame) {
        return FLog("========== Empty pFrame ============");
    } else {
        FLog("========== not Empty pFrame ============");
    }
    int iHours = pNetGame->m_pNetSet->byteWorldTime_Hour;

    if (iHours >= 0 && iHours <= 5)
        SetTexture("sky_weikton_ni");

    if (iHours >= 6 && iHours <= 10)
        SetTexture("sky_weikton_ev");

    if (iHours >= 11 && iHours <= 18)
        SetTexture("sky_weikton_da");

    if (iHours >= 19 && iHours <= 24)
        SetTexture("sky_weikton_ve");

    FLog("Inject sky hooks...");
    RwFrameForAllObjects(pFrame, reinterpret_cast<RwObjectCallBack>(Skybox::RwFrameForAllObjectsCallback), m_pSkyBox);
}

uintptr_t Skybox::RwFrameForAllObjectsCallback(uintptr_t object, CObject* pObject) {
    FLog("Skybox::RwFrameForAllObjectsCallback");
    RpAtomic* pAtomic = reinterpret_cast<RpAtomic*>(object);
    if (!pAtomic) return 0;

    RpGeometry* pGeom = RpAtomicGetGeometry(pAtomic);
    if (!pGeom) return 0;

    for (int i = 0; i < pGeom->matList.numMaterials; ++i) {
        RpMaterial* mat = pGeom->matList.materials[i];
        if (mat && pSkyTexture) {
            CHook::CallFunction<int>("_Z20RpMaterialSetTextureP10RpMaterialP9RwTexture", mat, pSkyTexture);
        }
    }
    return 1;
}

void Skybox::SetTexture(const char* texName) {
    if (!texName || !*texName) return;

    RwTexture* newTex = CUtil::LoadTextureFromDB("samp", texName);
    if (!newTex) {
        FLog("SetTexture - failed to load texture %s", texName);
        return;
    }

    if (pSkyTexture) {
        (*RwTextureDestroy)(pSkyTexture);
    }

    pSkyTexture = newTex;
}

bool Skybox::IsNeedRender() {
    return m_bNeedRender;
}

CObject* Skybox::GetSkyObject() {
    return m_pSkyBox;
}