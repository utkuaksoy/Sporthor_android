//
//  CommentPresenter.swift
//  Sporthor
//
//  Created by derTurke on 29.04.2025.
//
//

import Foundation

final class CommentPresenter: BasePresenter {
    // MARK: - VIPER Variables
    weak var view: CommentPresenterDelegate? {
        get { return self.baseView as? CommentPresenterDelegate }
        set { self.baseView = newValue }
    }
    
    var interactor: CommentInteractorProtocol {
        get { return self.baseInteractor as! CommentInteractorProtocol }
        set { self.baseInteractor = newValue }
    }
    
    var router: CommentRouterProtocol {
        get { return self.baseRouter as! CommentRouterProtocol }
        set { self.baseRouter = newValue }
    }
    
    // MARK: - Initialize
    init(view: CommentPresenterDelegate,
         interactor: CommentInteractorProtocol,
         router: CommentRouterProtocol,
         postId: String,
         delegate: CommentViewDelegate?) {
        self.postId = postId
        self.delegate = delegate
        super.init()
        self.view = view
        self.interactor = interactor
        self.router = router
        self.interactor.delegate = self
    }
    
    private var postId: String
    var comments: [CommentModel] = []
    private var comment: String = ""
    private var delegate: CommentViewDelegate?
}

// MARK: - CommentPresenterProtocol
extension CommentPresenter: CommentPresenterProtocol {
    func viewDidLoad() {
        view?.didSetTitleLabelText("Yorumlar")
        view?.prepareUI()
        prepareProfileImage()
        getComments()
    }
    
    private func getComments() {
        guard !postId.isEmpty else { return }
        let request: [String: Any] = ["PostId": postId]
        Task {
            @MainActor in
            await interactor.getComments(request)
        }
    }
    
    func prepareProfileImage() {
        view?.didSetProfilePhoto(ApplicationContext.shared.profilePhoto)
    }
    
    private func navigate(_ routes: CommentRoutes) {
        router.handleRouter(routes)
    }
    
    func textFieldDidChangeSelection(_ text: String) {
        view?.changeInputSendButtonHidden(text.isEmpty)
    }
    
    func textFieldDidEndEditing(_ text: String) {
        comment = text
    }
    
    func didTappedSendButton() {
        guard !comment.isEmpty else { return }
        let request: [String: Any] = ["postId": postId, "comment": comment]
        Task {
            @MainActor in
            await interactor.addComment(request)
        }
    }
    
    func panModalDismiss() {
        delegate?.reloadCommentCount(commentCount: comments.count, postId: postId)
    }
}

// MARK: - CommentInteractorDelegate
extension CommentPresenter: CommentInteractorDelegate {
    func didGetComments(_ response: [CommentModel]) {
        comments = response
        view?.reloadData()
    }
    
    func didAddComment() {
        getComments()
        view?.clearTextField()
    }
}
