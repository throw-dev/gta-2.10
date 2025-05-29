//
// Created by soul on 4/25/25.
//

#include "CSkyBox.h"
#include "../main.h"
#include "../game/game.h"
#include "patch.h"
#include "net/netgame.h"

extern CGame* pGame;
extern CNetGame *pNetGame;

CObject* CSkyBox::m_pSkyBox = nullptr;
RwTexture* pSkyTexture = nullptr;
float rx = 0.0f;

bool CSkyBox::is = true;

void CSkyBox::Process() {

    if (!m_pSkyBox) {
        CVector vec = {pGame->FindPlayerPed()->m_pPed->m_matrix->m_pos.x, pGame->FindPlayerPed()->m_pPed->m_matrix->m_pos.y, pGame->FindPlayerPed()->m_pPed->m_matrix->m_pos.z - 100.0f};
        CVector vec2 = {0.0f, 0.0f, 0.0f};
        m_pSkyBox = pGame->NewObject(18500, vec, vec2, 1.0f);
    } else {
        m_pSkyBox->MoveTo(pGame->FindPlayerPed()->m_pPed->m_matrix->m_pos.x, pGame->FindPlayerPed()->m_pPed->m_matrix->m_pos.y, pGame->FindPlayerPed()->m_pPed->m_matrix->m_pos.z, 1.0f, 0.0f, 0.0f, rx);
        rx = rx + 0.01f;
        //m_pSkyBox->SetPos(pGame->FindPlayerPed()->m_pPed->m_matrix->m_pos.x + 10.0f, pGame->FindPlayerPed()->m_pPed->m_matrix->m_pos.y + 10.0f, pGame->FindPlayerPed()->m_pPed->m_matrix->m_pos.z  + 10.0f);
        /*pSkyTexture = (RwTexture*)CUtil::LoadTextureFromDB("gui", "lkskybox");
        RwObject* rwObject = (RwObject*)m_pSkyBox->m_pEntity->m_pRwObject;
        CObject *pObject = pNetGame->GetObjectPool()->FindObjectFromGtaPtr(m_pSkyBox->m_pEntity);
        RwFrameForAllObjects(*(RwFrame*)rwObject->parent, (RwObject *(*)(RwObject *, void *))CSkyBox::ObjectMaterialCallBackEdgar, pObject);*/
        //RwPon();
    }

}
void CSkyBox::RwPon() {
    is = false;
    for (int i; i < 1000; i++) {
        m_pSkyBox->MoveTo(pGame->FindPlayerPed()->m_pPed->m_matrix->m_pos.x, pGame->FindPlayerPed()->m_pPed->m_matrix->m_pos.y, pGame->FindPlayerPed()->m_pPed->m_matrix->m_pos.z, 1.0f, 0.0f, 0.0f, 0.0f);
    }
}