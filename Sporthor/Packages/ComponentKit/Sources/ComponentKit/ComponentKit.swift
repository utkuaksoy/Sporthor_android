// The Swift Programming Language
// https://docs.swift.org/swift-book

import Foundation
import Factory

// MARK: - CKLabel
public protocol CKLabelDelegate: AnyObject {
    func didTapCKLabel(tag: Int)
}

public extension CKLabelDelegate {
    func didTapCKLabel(tag: Int) {}
}

// MARK: - CKTextField
public protocol CKTextFieldDelegate: AnyObject {
    func textFieldDidBeginEditing(_ textField: CKTextField)
    func textFieldDidEndEditing(_ textField: CKTextField)
    func textFieldDidChangeSelection(_ textField: CKTextField)
    func textFieldImageTapped(_ textField: CKTextField)
    func textFieldDidPressBackspace(in textField: CKTextField)
    func textFieldShouldPaste(_ textField: CKTextField, _ text: String) -> Bool
    func textField(_ textField: CKTextField, shouldChangeCharactersIn range: NSRange, replacementString string: String) -> Bool
}

public extension CKTextFieldDelegate {
    func textFieldDidBeginEditing(_ textField: CKTextField) {}
    func textFieldDidEndEditing(_ textField: CKTextField) {}
    func textFieldDidChangeSelection(_ textField: CKTextField) {}
    func textFieldImageTapped(_ textField: CKTextField) {}
    func textFieldDidPressBackspace(in textField: CKTextField) {}
    func textFieldShouldPaste(_ textField: CKTextField, _ text: String) -> Bool { return true }
    func textField(_ textField: CKTextField, shouldChangeCharactersIn range: NSRange, replacementString string: String) -> Bool { return true }
}

public enum CKTextFieldImagePosition {
    case left
    case right
}

// MARK: - CKButtonDelegate
public protocol CKButtonDelegate: AnyObject {
    func ckButtonDidTap(tag: Int)
}

public extension CKButtonDelegate {
    func ckButtonDidTap(tag: Int) {}
}

// MARK: - CKRadioButtonGroupDelegate
public protocol CKRadioButtonGroupDelegate: AnyObject {
    func radioButtonGroup(_ group: CKRadioButtonGroup, didSelect index: Int)
}

public protocol CKCircularCountdownViewDelegate: AnyObject {
    func circularCountdownViewDidFinish()
}

// MARK: - CKSearchBarDelegate
public protocol CKSearchBarDelegate: AnyObject {
    func searchBarDidBeginEditing(_ searchBar: CKSearchBar)
    func searchBarTextDidChange(_ searchBar: CKSearchBar, text: String)
    func searchBarTextDidEndEditing(_ searchBar: CKSearchBar, text: String)
    func searchBarDidCancel(_ searchBar: CKSearchBar)
}

public extension CKSearchBarDelegate {
    func searchBarDidBeginEditing(_ searchBar: CKSearchBar) {}
    func searchBarTextDidChange(_ searchBar: CKSearchBar, text: String) {}
    func searchBarTextDidEndEditing(_ searchBar: CKSearchBar, text: String) {}
    func searchBarDidCancel(_ searchBar: CKSearchBar) {}
}

// MARK: - CKHorizontalTitleButtonView
public protocol CKHorizontalTitleButtonViewDelegate: AnyObject {
    func ckHorizontalButtonClicked(tag: Int)
}

public extension CKHorizontalTitleButtonViewDelegate {
    func ckHorizontalButtonClicked(tag: Int) {}
}

// MARK: - CKSelectableBranchesView
public protocol CKSelectableBranchesViewDelegate: AnyObject {
    func tagButtonClicked(_ id: String)
    func addButtonClicked()
}

public extension CKSelectableBranchesViewDelegate {
    func tagButtonClicked(_ id: String) {}
    func addButtonClicked() {}
}

// MARK: - CKCustomTextViewDelegate
public protocol CKCustomTextViewDelegate: AnyObject {
    func textViewDidBeginEditing(_ ckCustomTextView: CKCustomTextView)
    func textViewDidChange(_ ckCustomTextView: CKCustomTextView)
    func textViewDidEndEditing(_ ckCustomTextView: CKCustomTextView)
    func textViewPreparing(_ ckCustomTextView: CKCustomTextView)
}

public extension CKCustomTextViewDelegate {
    func textViewDidBeginEditing(_ ckCustomTextView: CKCustomTextView) {}
    func textViewDidChange(_ ckCustomTextView: CKCustomTextView) {}
    func textViewDidEndEditing(_ ckCustomTextView: CKCustomTextView) {}
    func textViewPreparing(_ ckCustomTextView: CKCustomTextView) {}
}

// MARK: - CKPostLikeViewDelegate
public protocol CKPostLikeViewDelegate: AnyObject {
    func didTappedUsernameInLikeView(_ username: String, userId: String)
}

public extension CKPostLikeViewDelegate {
    func didTappedUsernameInLikeView(_ username: String, userId: String) {}
}

// MARK: - CKPostCommentViewDelegate
public protocol CKPostCommentViewDelegate: AnyObject {
    func didTappedUsernameInCommentView(_ username: String, userId: String)
}

public extension CKPostCommentViewDelegate {
    func didTappedUsernameInCommentView(_ username: String, userId: String) {}
}

// MARK: - CKPostHeaderViewDelegate
public protocol CKPostHeaderViewDelegate: AnyObject {
    func didTappedUsernameInHeaderView(_ username: String, userId: String)
    func didTappedTripleButton()
}

public extension CKPostCommentViewDelegate {
    func didTappedUsernameInHeaderView(_ username: String, userId: String) {}
}

// MARK: - CKStoryProfileViewDelegate
public protocol CKStoryProfileViewDelegate: AnyObject {
    func didTapStoryProfile()
}

public extension CKStoryProfileViewDelegate {
    func didTapStoryProfile() {}
}

// MARK: - CKProgressViewDelegate
public protocol CKProgressViewDelegate: AnyObject {
    func selectedStepDidFinish(step: Int)
    func didFinishProgress()
}

public extension CKProgressViewDelegate {
    func selectedStepDidFinish(step: Int) {}
    func didFinishProgress() {}
}

// MARK: - CKSelectionTextFieldDelegate
public protocol CKSelectionTextFieldDelegate: AnyObject {
    func ckSelectionTextFieldDidBeginEditing(_ text: String)
    func ckSelectionTextFieldDidEndEditing(_ text: String)
    func ckSelectionTextFieldDidChangeSelection(_ text: String)
}

public extension CKSelectionTextFieldDelegate {
    func ckSelectionTextFieldDidBeginEditing(_ text: String) {}
    func ckSelectionTextFieldDidEndEditing(_ text: String) {}
    func ckSelectionTextFieldDidChangeSelection(_ text: String) {}
}

// MARK: - CKDefaultAlertDelegate
public protocol CKDefaultAlertDelegate: AnyObject {
    func ckDefaultAlertDidTapOK()
    func ckDefaultAlertDidTapCancel()
}

public extension CKDefaultAlertDelegate {
    func ckDefaultAlertDidTapOK() {}
    func ckDefaultAlertDidTapCancel() {}
}
