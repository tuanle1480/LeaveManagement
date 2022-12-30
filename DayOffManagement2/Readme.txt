User (Nhân viên)
- Đăng nhập (Nếu role là 1 thì đăng nhập vào trang user, lấy tt user vừa đăng nhập)
- Home gồm cv cần làm
- Thanh navigation xổ ra gồm tính năng:
  + Logout (Quay lại trang đăng nhập thôi)
  + **ĐĂNG KÝ NGHỈ PHÉP** : Bao gồm ( từ ngày nào tới ngày nào, lí do xin nghỉ phép, sau đó nhấn nút xác nhận)
  + Xem tình trạng đơn nghỉ phép ( đơn của mình đc chấp thuận hay chưa)


Quản lí
- Đăng nhập ( nếu role id = 0 thì đăng nhập trang quản lí, quản lí nào ví dụ quản lí A, quản lí B có email và tên)
- Home gồm cv cần làm (giống user)
- Thanh navigation bên trái xổ ra gồm tính năng:
  + Quản lý nghỉ phép:
   - Xem đơn nghỉ phép ( Chấp thuận hoặc phê duyệt)
   - Tạo mới nghỉ phép với uid của nv nào đó
  + Quản lý tt nv
   - Thêm, sửa, xoá thông tin nhân viên
  + Quản lý lương(Làm đơn giản nhất có thể tính được lương ngày có phép hoặc không thôi):
   - Tổng kết lương nv
   - tính lương nv đã nghỉ phép:
	+ gồm ngày nghỉ có phép thì vẫn tính lương 1 tháng 28 ngày
	+ nghỉ không phép thì trừ vào số giờ đã nghỉ ( ví dụ 1 ngày làm 8 tiếng nv nghỉ 2 ngày gồm 16 tiếng thì trừ vào tổng số tiếng của tháng và tính lương)
	**Nv được nghỉ có phép 2 ngày trong tháng.