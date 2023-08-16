import VoteButtons from './VoteButtons';
import ShareButton from './ShareButton';
import Answer from './Answer/Answer';


export default function QuestionDetail({ question }) {
  if (!question) {
    return <div>Question not found.</div>;
  }

  return (
    <div className="w-[1000px] flex flex-col justify-center border-l-2">
      {/* 제목, 생성 날짜 */}
      <div className="flex justify-between ml-8 mt-12">
        <h2 className=" text-3xl">{question.title}</h2>
        <button className="w-[150px] h-[50px] text-[white] rounded-2xl bg-[#2196F3] ">
          Asked Question
        </button>
      </div>
      <div className="w-[550px] h-[40px] flex justify-around mt-4 ml-4 text-[#6179c9]">
        <span className="">Asked {question.CreatedAt}</span>
        <span className="">Modified {question.CreatedAt}</span>
        <span className="">Viewed {question.views}</span>
      </div>

      {/* border 용 div */}
      <div className="w-[1000px] ml-8 border-b-2"></div>

      {/* 질문 상세 내용*/}
      <div className="w-full h-[800px] flex ml-8">
        <VoteButtons />
        <div className="w-full h-[750px]">
          <p className="h-[700px] text-xl ml-4 mt-6">{question.content}</p>
          <div className="w-[1150px] flex justify-between">
            <div className="mr-4">
              {question.tags.map((tag, index) => (
                <button
                  key={index}
                  className="w-[100px] bg-[#afdcf7b4] rounded-full mr-4 text-[#95a6dd] hover:translate-x-1 hover:translate-y-1 leading-2"
                >
                  {tag}
                </button>
              ))}
            </div>
          </div>

          {/* Share, Edit Allow 버튼 */}
          <div className="w-full flex justify-between">
            <ShareButton />
            <div className="w-[150px] h-[80px] mr-20 bg-[#afdcf7b4]">
              <p className="text-[#949292]">{question.CreatedAt}</p>
              <p className="ml-4 text-sm text-[#4FC3F7]">{question.AskUser}</p>
            </div>
          </div>
        </div>
      </div>

      <Answer/>

      {/* Answer 입력 창 */}
      <div className="mt-8 border-t-2">
        <h1 className="text-3xl mt-6">Your Answer</h1>
        <input
          className="w-[1000px] h-[250px] mt-4 border-2 border-gray-300 rounded-md"
          type="text"
          placeholder="내용 입력"
        />
        <button className="w-[150px] h-[50px] mt-28 rounded-2xl bg-[#2196F3] text-[white]">
          Reply Answer
        </button>
      </div>
    </div>
  );
}
