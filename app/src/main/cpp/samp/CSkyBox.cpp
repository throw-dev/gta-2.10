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
        CVector vec = {pGame->FindPlayerPed()->m_pPed->m_matrix->m_pos.x, pGame->FindPlayerPed()->m_pPed->m_matrix->m_pos.y, pGame->FindPlayerPed()->m_pPed->m_matrix->m_pos.z - 10.0f};
        CVector vec2 = {0.0f, 0.0f, 0.0f};
        m_pSkyBox = pGame->NewObject(18659, vec, vec2, 1.0f);m_pSkyBox->SetMaterial(18659,10,"samp", "night_sky_1", 2);
    } else {
        m_pSkyBox->MoveTo(pGame->FindPlayerPed()->m_pPed->m_matrix->m_pos.x, pGame->FindPlayerPed()->m_pPed->m_matrix->m_pos.y, pGame->FindPlayerPed()->m_pPed->m_matrix->m_pos.z, 1.0f, 0.0f, 0.0f, rx);
        rx = rx + 0.01f;
    }

}
void CSkyBox::RwPon() {
    is = false;
    for (int i; i < 1000; i++) {
        m_pSkyBox->MoveTo(pGame->FindPlayerPed()->m_pPed->m_matrix->m_pos.x, pGame->FindPlayerPed()->m_pPed->m_matrix->m_pos.y, pGame->FindPlayerPed()->m_pPed->m_matrix->m_pos.z, 1.0f, 0.0f, 0.0f, 0.0f);
    }
}