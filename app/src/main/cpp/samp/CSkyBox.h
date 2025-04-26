//
// Created by soul on 4/25/25.
//

#ifndef SA_MP_LAUNCHER_CSKYBOX_H
#define SA_MP_LAUNCHER_CSKYBOX_H

#include "main.h"

class CObject;

class CSkyBox {
public:
    static void Process();

    static bool is;
private:
    static CObject* m_pSkyBox;

    static uintptr_t RwFrameForAllObjectsCallback(uintptr_t object, CObject *pObject);

    static RpAtomic *ObjectMaterialCallBackEdgar(RpAtomic *rpAtomic, CObject *pObject);

    static void RwPon();
};

#endif //SA_MP_LAUNCHER_CSKYBOX_H