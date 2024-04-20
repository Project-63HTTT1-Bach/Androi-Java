var radio1 = document.getElementsByClassName('radio1')
var radio2 = document.getElementsByClassName('radio2')
function CheckRadio(x){
    var y=0
    for (bien in x){
        if(bien.checked){
           y+=1 
           console.log(bien.checked)
        }
    }
    if(y==0){
        return false
    }
    return true
}
function Submit(){
    var array = new Array()
    for(let i=1; i<=8 ; i++){
        array.push(document.getElementById('input'+i))
    }
    if (array[0].value.trim()===''){
        alert('Hãy nhập tên đơn vị')
        array[0].focus()
        return false
    }
    else if (array[1].value.trim()===''){
        alert('Hãy nhập mã số thuế')
        array[1].focus()
        return false
    }
    else if (array[2].value.trim()===''){
        alert('Hãy nhập địa chỉ')
        array[2].focus()
        return false
    }
    else if (CheckRadio(radio1)==false){
        alert('Hãy chọn ngành nghề')
        var m = document.getElementById('Nganh_nghe-Span')
        m.focus()
        m.innerText = 'Hãy chọn ngành nghề'
        return false
    }
    else if (CheckRadio(radio1)==true){
        var m = document.getElementById('Nganh_nghe-Span')
        m.focus()
        m.innerText = ''
        return false
    }
    else if (CheckRadio(radio2)==false){
        alert('Hãy chọn phường')
        
        return false
    }
    else if (array[3].value.trim()===''){
        alert('Hãy nhập tổng số người lao động tại đơn vị')
        array[3].focus()
        return false
    }
    else if (array[4].value.trim()===''){
        alert('Hãy nhập số người đã tiêm vắc xin COVID-19')
        array[4].focus()
        return false
    }
    else if (array[5].value.trim()===''){
        alert('Hãy nhập số người đồng thuận tiên vắc xin COVID-19')
        array[5].focus()
        return false
    }
    else if (array[6].value.trim()===''){
        alert('Hãy nhập số điện thoại')
        array[6].focus()
        return false
    }
    else if (array[7].value.trim()===''){
        alert('Hãy nhập email')
        array[7].focus()
        return false
    }
}

