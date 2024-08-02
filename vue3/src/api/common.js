export function getIdTypeName(idType){
    if (idType == 1) {
        return '身份证';
    }
    if (idType == 2) {
        return '港澳台居民居住证';
    }
    if (idType == 3) {
        return '港澳居民来往内地通行证';
    }
    if (idType == 4) {
        return '台湾居民来往内地通行证';
    }
    if (idType == 5) {
        return '护照';
    }
    if (idType == 6) {
        return '外国人永久居住证';
    }
} 