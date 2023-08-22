import { useSelector } from 'react-redux';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';
import { RouteConst } from '../../../Interface/RouteConst';

export default function ShareButton({
  answerEditMode,
  setAnswerEditMode,
  answerId,
  memberId,
}) {
  const myId = useSelector((state) => state.logininfo.myid);
  const token = useSelector((state) => state.logininfo.token);
  const content = useSelector((state) => state.answerCRUD.answerContent);

  const navigate = useNavigate();

  // Edit 버튼 눌렀을 때 disptch로 보낼거임
  // 내용은 편집모드로 바꿈 (true일 때)
  // save 눌렀을 떈 false로 바꿈
  // 이 내용이 버튼에 onClick으로 들어가야함

  const handleAnswerPatch = () => {
    return axios.patch(
      `http://ec2-3-39-228-109.ap-northeast-2.compute.amazonaws.com/answers/${answerId}`,
      { content },
      {
        headers: {
          Authorization: token,
        },
      }
    );
  };

  const handleAnswerDelete = () => {
    return axios
      .delete(
        `http://ec2-43-201-249-199.ap-northeast-2.compute.amazonaws.com/answers/${answerId}`,
        {
          headers: {
            Authorization: token,
          },
        }
      )
      .then((res) => {
        console.log(res);
        navigate(RouteConst.Main);
      })
      .catch((err) => {
        console.log(err);
      });
  };

  return (
    <>
      <div className="w-[200px] flex justify-around my-6 ">
        <button className="text-[#e0e0e0] hover:text-[black]">Share</button>
        <button className="text-[#e0e0e0] hover:text-[black]">With</button>
        <button className="text-[#e0e0e0] hover:text-[black]">Follow</button>
        {myId === memberId && (
          <>
            <button
              onClick={() => {
                setAnswerEditMode(!answerEditMode);
                if (answerEditMode) {
                  handleAnswerPatch();
                }
              }}
              className="text-[#e0e0e0] hover:text-[black]"
            >
              {answerEditMode ? 'Save' : 'Edit'}
            </button>
            <button
              onClick={handleAnswerDelete}
              className="text-[#e0e0e0] hover:text-[black]"
            >
              Delete
            </button>
          </>
        )}
      </div>
    </>
  );
}
