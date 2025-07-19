// psychobye

#include "CSkyBox.h"
#include "main.h"
#include "game.h"
#include "patch.h"
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

    ReTexture();
}

void Skybox::ReTexture() {
    if (!m_pSkyBox || !m_pSkyBox->m_pEntity || !m_pSkyBox->m_pEntity->m_pRwAtomic) return;

    RpAtomic* pAtomic = m_pSkyBox->m_pEntity->m_pRwAtomic;
    if (!pAtomic || !*(uintptr_t*)((uintptr_t)pAtomic + 4)) return;

    RwFrame* pFrame = *(RwFrame**)((uintptr_t)pAtomic + 8);
    if (!pFrame) return;

    int iHours = pNetGame->m_pNetSet->byteWorldTime_Hour;

    if (iHours >= 6 && iHours <= 11) SetTexture("rasv");
    else if (iHours >= 12 && iHours <= 17) SetTexture("day");
    else if (iHours >= 18 && iHours <= 22) SetTexture("zakat");
    else SetTexture("nght");

    RwFrameForAllObjects(
            pFrame,
            reinterpret_cast<RwObjectCallBack>(Skybox::RwFrameForAllObjectsCallback),
            m_pSkyBox
    );
}

uintptr_t Skybox::RwFrameForAllObjectsCallback(uintptr_t object, CObject* pObject) {
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