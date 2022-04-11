/**
 * The retrofit package contains the {@link org.shakespeareframework.retrofit.CallHttpApis} {@link
 * org.shakespeareframework.Ability}, which enables an {@link org.shakespeareframework.Actor} to
 * call HTTP APIs using {@link retrofit2.Retrofit} generated clients.
 *
 * <p>Additionally the {@link org.shakespeareframework.retrofit.HeaderInterceptor} can be used via
 * {@link org.shakespeareframework.retrofit.CallHttpApis.Builder#addInterceptor} to add standard
 * headers that will be added to any request. E.g. to add authentication headers.
 *
 * @see <a href="https://shakespeareframework.org/latest/manual/#_http_api_testing_with_retrofit">
 *   Manual on "HTTP API Testing with Retrofit"</a>
 */
package org.shakespeareframework.retrofit;
