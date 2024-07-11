#pragma version(1)
#pragma rs java_package_name(com.example.app)

rs_allocation inputImage;

// Коэффициент размытия
float blurRadius;

// Функция размытия
uchar4 RS_KERNEL blur(uchar4 in, uint32_t x, uint32_t y) {
    float4 sum = 0;
    int radius = (int)blurRadius;

    for (int i = -radius; i <= radius; i++) {
        for (int j = -radius; j <= radius; j++) {
            float4 pixel = rsUnpackColor8888(rsGetElementAt_uchar4(inputImage, x + i, y + j));
            sum += pixel;
        }
    }

    float4 avg = sum / ((2 * radius + 1) * (2 * radius + 1));
    return rsPackColorTo8888(avg);
}