//
// Created by vadim on 25.03.2025.
//

#pragma once

#include "../main.h"

class CObject;

class CHooks {
public:
    //static void InitialiseSkyBox();


    int InitialiseSkyBox();

private:
    static CObject* CreateObjectScaled(int iModel, float fScale);

private:
    void InitialiseSkyBoxxx();
};
